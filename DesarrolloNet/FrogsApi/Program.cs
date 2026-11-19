using FrogsApi.Data;
using Microsoft.EntityFrameworkCore;
using FrogsApi.Services;

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

builder.Services.AddScoped<IFrogsService, FrogsService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

if (!app.Environment.IsDevelopment())
{
    app.UseHttpsRedirection();
}

app.UseAuthorization();

app.MapControllers();

app.Run();
