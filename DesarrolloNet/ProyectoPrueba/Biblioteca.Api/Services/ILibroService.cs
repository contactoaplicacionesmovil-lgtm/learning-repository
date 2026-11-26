using Biblioteca.Api.DTOs;

namespace Biblioteca.Api.Services;

public interface ILibroService
{
    Task<IReadOnlyList<LibroDto>> ObtenerTodosAsync(
        CancellationToken cancellationToken);

    Task<LibroDto?> ObtenerPorIdAsync(
        int id,
        CancellationToken cancellationToken);

    Task<LibroDto?> CrearAsync(
    CrearLibroDto request,
    CancellationToken cancellationToken);

    Task<ResultadoActualizarLibro> ActualizarAsync(
    int id,
    ActualizarLibroDto request,
    CancellationToken cancellationToken);

    Task<bool> EliminarAsync(
    int id,
    CancellationToken cancellationToken);

}