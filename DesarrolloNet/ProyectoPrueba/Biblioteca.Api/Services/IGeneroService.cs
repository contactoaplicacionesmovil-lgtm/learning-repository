using Biblioteca.Api.DTOs;

namespace Biblioteca.Api.Services;

public interface IGeneroService
{
    Task<IReadOnlyList<GeneroDto>> ObtenerTodosAsync(
        CancellationToken cancellationToken);

    Task<GeneroDto?> ObtenerPorIdAsync(
        int id,
        CancellationToken cancellationToken);

    Task<GeneroDto?> CrearAsync(
        CrearGeneroDto solicitud,
        CancellationToken cancellationToken);
}