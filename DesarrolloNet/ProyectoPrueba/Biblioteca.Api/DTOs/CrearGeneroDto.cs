using System.ComponentModel.DataAnnotations;

namespace Biblioteca.Api.DTOs;

public sealed record CrearGeneroDto
{
    [Required, StringLength(100, MinimumLength = 1)]
    public required string Nombre { get; init; }
}