using Biblioteca.Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Biblioteca.Api.Data;

public sealed class BibliotecaDbContext(
    DbContextOptions<BibliotecaDbContext> options
) : DbContext(options)
{
    public DbSet<Libro> Libros => Set<Libro>();
    public DbSet<Genero> Generos => Set<Genero>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Genero>(entidad =>
        {
            entidad.Property(genero => genero.Nombre)
                .HasMaxLength(100)
                .IsRequired();

            entidad.HasIndex(genero => genero.Nombre)
                .IsUnique();
        });

        modelBuilder.Entity<Libro>(entidad =>
        {
            entidad.Property(libro => libro.Titulo)
                .HasMaxLength(200)
                .IsRequired();

            entidad.Property(libro => libro.Autor)
                .HasMaxLength(150)
                .IsRequired();

            entidad.HasOne(libro => libro.Genero)
                .WithMany(genero => genero.Libros)
                .HasForeignKey(libro => libro.GeneroId)
                .OnDelete(DeleteBehavior.Restrict);
        });
    }
}