namespace Biblioteca.Api.Models;
public sealed class Genero
{
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public ICollection<Libro> Libros { get; set; } = new List<Libro>();
}