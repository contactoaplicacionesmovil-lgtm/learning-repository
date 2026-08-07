using System.Diagnostics;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddOpenApi();
builder.Services.AddControllers();

var jwtSection = builder.Configuration.GetSection("Jwt");
var jwtKey = jwtSection["Key"];
var jwtIssuer = jwtSection["Issuer"];
var jwtAudience = jwtSection["Audience"];

if (string.IsNullOrWhiteSpace(jwtKey) || string.IsNullOrWhiteSpace(jwtIssuer) || string.IsNullOrWhiteSpace(jwtAudience))
{
    throw new InvalidOperationException("La configuración JWT es inválida. Revisa Jwt:Key, Jwt:Issuer y Jwt:Audience.");
}

var signingKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(jwtKey));

builder.Services
    .AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidateAudience = true,
            ValidateIssuerSigningKey = true,
            ValidateLifetime = true,
            ValidIssuer = jwtIssuer,
            ValidAudience = jwtAudience,
            IssuerSigningKey = signingKey,
            ClockSkew = TimeSpan.Zero
        };

        options.Events = new JwtBearerEvents
        {
            OnChallenge = async context =>
            {
                context.HandleResponse();
                context.Response.StatusCode = StatusCodes.Status401Unauthorized;
                context.Response.ContentType = "application/problem+json";

                var problem = new ProblemDetails
                {
                    Status = StatusCodes.Status401Unauthorized,
                    Title = "No autorizado.",
                    Detail = "Se requiere un token válido para acceder a este recurso.",
                    Instance = context.Request.Path
                };

                problem.Extensions["traceId"] = context.HttpContext.TraceIdentifier;

                await context.Response.WriteAsJsonAsync(problem);
            },
            OnForbidden = async context =>
            {
                context.Response.StatusCode = StatusCodes.Status403Forbidden;
                context.Response.ContentType = "application/problem+json";

                var problem = new ProblemDetails
                {
                    Status = StatusCodes.Status403Forbidden,
                    Title = "Prohibido.",
                    Detail = "No tienes permisos para acceder a este recurso.",
                    Instance = context.Request.Path
                };

                problem.Extensions["traceId"] = context.HttpContext.TraceIdentifier;

                await context.Response.WriteAsJsonAsync(problem);
            }
        };
    });

builder.Services.AddAuthorization();

var app = builder.Build();

app.UseExceptionHandler(exceptionApp =>
{
    exceptionApp.Run(async context =>
    {
        context.Response.StatusCode = StatusCodes.Status500InternalServerError;
        context.Response.ContentType = "application/problem+json";

        var exceptionFeature = context.Features.Get<Microsoft.AspNetCore.Diagnostics.IExceptionHandlerPathFeature>();

        var problemDetails = new Microsoft.AspNetCore.Mvc.ProblemDetails
        {
            Status = StatusCodes.Status500InternalServerError,
            Title = "Ocurrió un error inesperado.",
            Detail = app.Environment.IsDevelopment() ? exceptionFeature?.Error.Message : null,
            Instance = context.Request.Path
        };

        problemDetails.Extensions["traceId"] = context.TraceIdentifier;

        await context.Response.WriteAsJsonAsync(problemDetails);
    });
});

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

app.UseHttpsRedirection();

app.UseAuthentication();
app.UseAuthorization();

app.Use(async (context, next) =>
{
    const int maxLoggedBodyLength = 4_000;

    var logger = context.RequestServices
        .GetRequiredService<ILoggerFactory>()
        .CreateLogger("AuditLogger");

    var stopwatch = Stopwatch.StartNew();
    var request = context.Request;
    var requestHeaders = BuildSanitizedHeaders(request.Headers);
    var requestBody = await ReadRequestBodyAsync(request, maxLoggedBodyLength);

    logger.LogInformation(
        "Incoming request: {Method} {Path}{QueryString} TraceId={TraceId} Headers={Headers} Body={Body}",
        request.Method,
        request.Path,
        request.QueryString.HasValue ? request.QueryString.Value : string.Empty,
        context.TraceIdentifier,
        requestHeaders,
        requestBody);

    var originalResponseBody = context.Response.Body;

    await using var responseBodyStream = new MemoryStream();
    context.Response.Body = responseBodyStream;

    try
    {
        await next();
    }
    finally
    {
        stopwatch.Stop();

        context.Response.Body.Seek(0, SeekOrigin.Begin);
        var responseBody = await ReadResponseBodyAsync(context.Response, maxLoggedBodyLength);

        logger.LogInformation(
            "Outgoing response: {Method} {Path}{QueryString} StatusCode={StatusCode} DurationMs={DurationMs} TraceId={TraceId} Body={Body}",
            request.Method,
            request.Path,
            request.QueryString.HasValue ? request.QueryString.Value : string.Empty,
            context.Response.StatusCode,
            stopwatch.ElapsedMilliseconds,
            context.TraceIdentifier,
            responseBody);

        context.Response.Body.Seek(0, SeekOrigin.Begin);
        await context.Response.Body.CopyToAsync(originalResponseBody);
        context.Response.Body = originalResponseBody;
    }
});

app.MapControllers();

app.MapPost("/api/auth/token", ([FromBody] LoginRequest request, IConfiguration configuration, HttpContext httpContext) =>
{
    var authSection = configuration.GetSection("Auth");
    var configuredUser = authSection["Username"];
    var configuredPassword = authSection["Password"];

    if (string.IsNullOrWhiteSpace(configuredUser) || string.IsNullOrWhiteSpace(configuredPassword))
    {
        return Results.Problem(
            statusCode: StatusCodes.Status500InternalServerError,
            title: "Configuración inválida.",
            detail: "Credenciales de autenticación no configuradas.",
            instance: httpContext.Request.Path,
            extensions: new Dictionary<string, object?>
            {
                ["traceId"] = httpContext.TraceIdentifier
            });
    }

    if (!string.Equals(request.Username, configuredUser, StringComparison.Ordinal) ||
        !string.Equals(request.Password, configuredPassword, StringComparison.Ordinal))
    {
        return Results.Problem(
            statusCode: StatusCodes.Status401Unauthorized,
            title: "Credenciales inválidas.",
            detail: "Usuario o contraseña incorrectos.",
            instance: httpContext.Request.Path,
            extensions: new Dictionary<string, object?>
            {
                ["traceId"] = httpContext.TraceIdentifier
            });
    }

    var expiresInMinutes = configuration.GetValue<int?>("Jwt:ExpiresInMinutes") ?? 60;
    var expiresAtUtc = DateTime.UtcNow.AddMinutes(expiresInMinutes);

    var claims = new List<Claim>
    {
        new(JwtRegisteredClaimNames.Sub, request.Username),
        new(JwtRegisteredClaimNames.UniqueName, request.Username),
        new(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
    };

    var tokenDescriptor = new SecurityTokenDescriptor
    {
        Subject = new ClaimsIdentity(claims),
        Expires = expiresAtUtc,
        Issuer = jwtIssuer,
        Audience = jwtAudience,
        SigningCredentials = new SigningCredentials(signingKey, SecurityAlgorithms.HmacSha256)
    };

    var tokenHandler = new JwtSecurityTokenHandler();
    var token = tokenHandler.CreateToken(tokenDescriptor);

    return Results.Ok(new
    {
        access_token = tokenHandler.WriteToken(token),
        token_type = "Bearer",
        expires_at = expiresAtUtc
    });
})
.AllowAnonymous();

app.Run();

static async Task<string> ReadRequestBodyAsync(HttpRequest request, int maxLength)
{
    if (request.ContentLength is 0 || request.Body is null)
    {
        return "<empty>";
    }

    if (!IsTextBasedContentType(request.ContentType))
    {
        return "<non-text content omitted>";
    }

    request.EnableBuffering();
    request.Body.Seek(0, SeekOrigin.Begin);

    using var reader = new StreamReader(request.Body, Encoding.UTF8, detectEncodingFromByteOrderMarks: false, leaveOpen: true);
    var body = await reader.ReadToEndAsync();
    request.Body.Seek(0, SeekOrigin.Begin);

    return TrimForLogging(body, maxLength);
}

static async Task<string> ReadResponseBodyAsync(HttpResponse response, int maxLength)
{
    if (response.Body.Length == 0)
    {
        return "<empty>";
    }

    if (!IsTextBasedContentType(response.ContentType))
    {
        return "<non-text content omitted>";
    }

    using var reader = new StreamReader(response.Body, Encoding.UTF8, detectEncodingFromByteOrderMarks: false, leaveOpen: true);
    var body = await reader.ReadToEndAsync();

    return TrimForLogging(body, maxLength);
}

static Dictionary<string, string> BuildSanitizedHeaders(IHeaderDictionary headers)
{
    var redactedHeaders = new HashSet<string>(StringComparer.OrdinalIgnoreCase)
    {
        "Authorization",
        "Cookie",
        "Set-Cookie"
    };

    var sanitized = new Dictionary<string, string>(StringComparer.OrdinalIgnoreCase);

    foreach (var header in headers)
    {
        sanitized[header.Key] = redactedHeaders.Contains(header.Key)
            ? "<redacted>"
            : string.Join(",", header.Value.ToArray());
    }

    return sanitized;
}

static bool IsTextBasedContentType(string? contentType)
{
    if (string.IsNullOrWhiteSpace(contentType))
    {
        return true;
    }

    return contentType.Contains("json", StringComparison.OrdinalIgnoreCase)
           || contentType.Contains("xml", StringComparison.OrdinalIgnoreCase)
           || contentType.Contains("text/", StringComparison.OrdinalIgnoreCase)
           || contentType.Contains("x-www-form-urlencoded", StringComparison.OrdinalIgnoreCase);
}

static string TrimForLogging(string input, int maxLength)
{
    if (string.IsNullOrEmpty(input))
    {
        return "<empty>";
    }

    return input.Length <= maxLength
        ? input
        : $"{input[..maxLength]}...(truncated)";
}

public sealed class LoginRequest
{
    public string Username { get; init; } = string.Empty;

    public string Password { get; init; } = string.Empty;
}
