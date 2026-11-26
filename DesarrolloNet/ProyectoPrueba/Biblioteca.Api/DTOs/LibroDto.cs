namespace Biblioteca.Api.DTOs;

/// <summary>Datos de un libro que devuelve la API.</summary>
public sealed record LibroDto(
    int Id,
    string Titulo,
    string Autor,
    int GeneroId,
    string GeneroNombre);