using Biblioteca.Api.Data;
using Microsoft.EntityFrameworkCore;
using Biblioteca.Api.Services;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();

//Se registra BibliotecaDbContext
builder.Services.AddDbContext<BibliotecaDbContext>(opciones =>
    opciones.UseSqlServer(
        builder.Configuration.GetConnectionString("Biblioteca")));

// se registrar GeneroService, LibroService
builder.Services.AddScoped<IGeneroService, GeneroService>();
builder.Services.AddScoped<ILibroService, LibroService>();

// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddOpenApi();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
