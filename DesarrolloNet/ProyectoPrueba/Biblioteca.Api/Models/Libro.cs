namespace Biblioteca.Api.Models;

public sealed class Libro
{
    public int Id {get; set;}
    public string Titulo { get; set; } = string.Empty;
    public string Autor { get; set; } = string.Empty;

    public int GeneroId { get; set; }
    public Genero Genero { get; set; } = null!;
}