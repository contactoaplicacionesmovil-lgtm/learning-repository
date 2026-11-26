using Biblioteca.Api.DTOs;

namespace Biblioteca.Api.Services;

public enum EstadoActualizarLibro
{
    Actualizado,
    LibroNoEncontrado,
    GeneroNoEncontrado
}

public sealed record ResultadoActualizarLibro(
    EstadoActualizarLibro Estado,
    LibroDto? Libro);

