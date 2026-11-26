using Biblioteca.Api.Data;
using Biblioteca.Api.DTOs;
using Biblioteca.Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Biblioteca.Api.Services;

public sealed class GeneroService : IGeneroService
{
    private readonly BibliotecaDbContext _context;

    public GeneroService(BibliotecaDbContext context)
    {
        _context = context;
    }

    public async Task<IReadOnlyList<GeneroDto>> ObtenerTodosAsync(
        CancellationToken cancellationToken)
    {
        return await _context.Generos
            .AsNoTracking()
            .OrderBy(genero => genero.Nombre)
            .Select(genero => new GeneroDto(genero.Id, genero.Nombre))
            .ToListAsync(cancellationToken);
    }

    public async Task<GeneroDto?> ObtenerPorIdAsync(
        int id,
        CancellationToken cancellationToken)
    {
        return await _context.Generos
            .AsNoTracking()
            .Where(genero => genero.Id == id)
            .Select(genero => new GeneroDto(genero.Id, genero.Nombre))
            .FirstOrDefaultAsync(cancellationToken);
    }

    public async Task<GeneroDto?> CrearAsync(
        CrearGeneroDto solicitud,
        CancellationToken cancellationToken)
    {
        var nombre = solicitud.Nombre.Trim();

        var yaExiste = await _context.Generos
            .AnyAsync(genero => genero.Nombre == nombre, cancellationToken);

        if (yaExiste)
        {
            return null;
        }

        var genero = new Genero { Nombre = nombre };

        _context.Generos.Add(genero);
        await _context.SaveChangesAsync(cancellationToken);

        return new GeneroDto(genero.Id, genero.Nombre);
    }
}