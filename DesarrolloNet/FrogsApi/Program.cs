using FrogsApi.Data;
using Microsoft.EntityFrameworkCore;
using FrogsApi.Services;
using FrogsApi.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using Microsoft.AspNetCore.Authentication.JwtBearer;

var builder = WebApplication.CreateBuilder(args);
builder.Logging.ClearProviders();
builder.Logging.AddConsole();

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddOpenApi();

var connectionString = builder.Configuration.GetConnectionString("FrogsDatabase")
    ?? throw new InvalidOperationException(
        "No se encontró la cadena de conexión 'FrogsDatabase'.");

builder.Services.AddDbContext<FrogsDbContext>(options =>
    options.UseSqlServer(connectionString));

builder.Services.AddIdentityCore<ApplicationUser>(options =>
{
    options.User.RequireUniqueEmail = true;
    options.Password.RequiredLength = 6;
     options.Password.RequireNonAlphanumeric = false;
     options.Password.RequireUppercase = false;
})
.AddRoles<IdentityRole>()
.AddEntityFrameworkStores<FrogsDbContext>()
.AddSignInManager();

builder.Services.AddScoped<IFrogsService, FrogsService>();
builder.Services.AddScoped<IAuthService, AuthService>();

var issuer = builder.Configuration["Jwt:Issuer"]
    ?? throw new InvalidOperationException("Falta configurar Jwt:Issuer.");

var audience = builder.Configuration["Jwt:Audience"]
    ?? throw new InvalidOperationException("Falta configurar Jwt:Audience.");

var signingKeyBase64 = builder.Configuration["Jwt:SigningKey"]
    ?? throw new InvalidOperationException(
        "Falta Jwt:SigningKey en User Secrets o en la configuración del entorno.");

var signingKeyBytes = Convert.FromBase64String(signingKeyBase64);

if (signingKeyBytes.Length < 32)
{
    throw new InvalidOperationException(
        "Jwt:SigningKey debe contener al menos 32 bytes aleatorios.");
}

var signingKey = new SymmetricSecurityKey(signingKeyBytes);

builder.Services
    .AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidIssuer = issuer,
            ValidateAudience = true,
            ValidAudience = audience,
            ValidateIssuerSigningKey = true,
            IssuerSigningKey = signingKey,
            ValidateLifetime = true,
            RequireExpirationTime = true,
            ClockSkew = TimeSpan.FromSeconds(30)
        };
    });

builder.Services.AddAuthorization();

var app = builder.Build();


if (app.Environment.IsDevelopment())
{
    var email = app.Configuration["Bootstrap:Email"]
        ?? throw new InvalidOperationException("Falta Bootstrap:Email en User Secrets.");
    var password = app.Configuration["Bootstrap:Password"]
        ?? throw new InvalidOperationException("Falta Bootstrap:Password en User Secrets.");

    using var scope = app.Services.CreateScope();
    var userManager = scope.ServiceProvider
        .GetRequiredService<UserManager<ApplicationUser>>();

    if (await userManager.FindByEmailAsync(email) is null)
    {
        var user = new ApplicationUser
        {
            UserName = email,
            Email = email
        };

        var result = await userManager.CreateAsync(user, password);

        if (!result.Succeeded)
        {
            var errors = string.Join(", ", result.Errors.Select(error => error.Description));
            throw new InvalidOperationException($"No se pudo crear la cuenta inicial: {errors}");
        }
    }
}

if (!app.Environment.IsDevelopment())
{
    app.UseHttpsRedirection();
}

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();
