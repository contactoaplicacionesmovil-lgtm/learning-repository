using Biblioteca.Api.Data;
using Biblioteca.Api.DTOs;
using Microsoft.EntityFrameworkCore;
using Biblioteca.Api.Models;

namespace Biblioteca.Api.Services;

public sealed class LibroService : ILibroService
{
    private readonly BibliotecaDbContext _context;

    public LibroService(BibliotecaDbContext context)
    {
        _context = context;
    }

    public async Task<IReadOnlyList<LibroDto>> ObtenerTodosAsync(
        CancellationToken cancellationToken)
    {
        return await _context.Libros
            .AsNoTracking()
            .OrderBy(libro => libro.Titulo)
            .Select(libro => new LibroDto(
                libro.Id,
                libro.Titulo,
                libro.Autor,
                libro.GeneroId,
                libro.Genero.Nombre))
            .ToListAsync(cancellationToken);
    }

    public async Task<LibroDto?> ObtenerPorIdAsync(
        int id,
        CancellationToken cancellationToken)
    {
        return await _context.Libros
            .AsNoTracking()
            .Where(libro => libro.Id == id)
            .Select(libro => new LibroDto(
                libro.Id,
                libro.Titulo,
                libro.Autor,
                libro.GeneroId,
                libro.Genero.Nombre))
            .FirstOrDefaultAsync(cancellationToken);
    }

    public async Task<LibroDto?> CrearAsync(
        CrearLibroDto request,
        CancellationToken cancellationToken)
    {
        var genero = await _context.Generos
            .Where(genero => genero.Id == request.GeneroId)
            .Select(genero => new { genero.Id, genero.Nombre })
            .FirstOrDefaultAsync(cancellationToken);

        if (genero is null)
        {
            return null;
        }

        var libro = new Libro
        {
            Titulo = request.Titulo.Trim(),
            Autor = request.Autor.Trim(),
            GeneroId = genero.Id
        };

        _context.Libros.Add(libro);
        await _context.SaveChangesAsync(cancellationToken);

        return new LibroDto(
            libro.Id,
            libro.Titulo,
            libro.Autor,
            libro.GeneroId,
            genero.Nombre);
    }

    public async Task<ResultadoActualizarLibro> ActualizarAsync(
        int id,
        ActualizarLibroDto request,
        CancellationToken cancellationToken)
    {
        var libro = await _context.Libros
            .FirstOrDefaultAsync(libro => libro.Id == id, cancellationToken);

        if (libro is null)
        {
            return new ResultadoActualizarLibro(
                EstadoActualizarLibro.LibroNoEncontrado,
                null);
        }

        var genero = await _context.Generos
            .Where(genero => genero.Id == request.GeneroId)
            .Select(genero => new { genero.Id, genero.Nombre })
            .FirstOrDefaultAsync(cancellationToken);

        if (genero is null)
        {
            return new ResultadoActualizarLibro(
                EstadoActualizarLibro.GeneroNoEncontrado,
                null);
        }

        libro.Titulo = request.Titulo.Trim();
        libro.Autor = request.Autor.Trim();
        libro.GeneroId = genero.Id;

        await _context.SaveChangesAsync(cancellationToken);

        var respuesta = new LibroDto(
            libro.Id,
            libro.Titulo,
            libro.Autor,
            libro.GeneroId,
            genero.Nombre);

        return new ResultadoActualizarLibro(
            EstadoActualizarLibro.Actualizado,
            respuesta);
    }


    public async Task<bool> EliminarAsync(
        int id,
        CancellationToken cancellationToken)
    {
        var libro = await _context.Libros
            .FirstOrDefaultAsync(libro => libro.Id == id, cancellationToken);

        if (libro is null)
        {
            return false;
        }

        _context.Libros.Remove(libro);
        await _context.SaveChangesAsync(cancellationToken);

        return true;
    }

}