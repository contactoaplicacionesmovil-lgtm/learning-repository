using System.ComponentModel.DataAnnotations;

namespace Biblioteca.Api.DTOs;

/// <summary>Datos necesarios para crear un libro.</summary>
public sealed record CrearLibroDto
{
    [Required, StringLength(200, MinimumLength = 1)]
    public required string Titulo { get; init; }

    [Required, StringLength(150, MinimumLength = 1)]
    public required string Autor { get; init; }

    [Range(1, int.MaxValue)]
    public int GeneroId { get; init; }
}