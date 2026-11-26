using Biblioteca.Api.DTOs;
using Biblioteca.Api.Services;
using Microsoft.AspNetCore.Mvc;

namespace Biblioteca.Api.Controllers;

[ApiController]
[Route("api/libros")]
public sealed class LibrosController : ControllerBase
{
    private readonly ILibroService _libroService;

    public LibrosController(ILibroService libroService)
    {
        _libroService = libroService;
    }

    [HttpGet]
    [ProducesResponseType(typeof(IReadOnlyList<LibroDto>), StatusCodes.Status200OK)]
    public async Task<ActionResult<IReadOnlyList<LibroDto>>> ObtenerTodos(
        CancellationToken cancellationToken)
    {
        var libros = await _libroService.ObtenerTodosAsync(cancellationToken);
        return Ok(libros);
    }

    [HttpGet("{id:int}")]
    [ProducesResponseType(typeof(LibroDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<LibroDto>> ObtenerPorId(
        int id,
        CancellationToken cancellationToken)
    {
        var libro = await _libroService.ObtenerPorIdAsync(id, cancellationToken);

        if (libro is null)
        {
            return NotFound();
        }

        return Ok(libro);
    }


    [HttpPost]
    [ProducesResponseType(typeof(LibroDto), StatusCodes.Status201Created)]
    [ProducesResponseType(typeof(ProblemDetails), StatusCodes.Status400BadRequest)]
    public async Task<ActionResult<LibroDto>> Crear(
        CrearLibroDto request,
        CancellationToken cancellationToken)
    {
        var libro = await _libroService.CrearAsync(request, cancellationToken);

        if (libro is null)
        {
            return BadRequest(new ProblemDetails
            {
                Status = StatusCodes.Status400BadRequest,
                Title = "Género inválido",
                Detail = "No existe un género con el GeneroId enviado."
            });
        }

        return CreatedAtAction(
            nameof(ObtenerPorId),
            new { id = libro.Id },
            libro);
    }

    [HttpPut("{id:int}")]
    [ProducesResponseType(typeof(LibroDto), StatusCodes.Status200OK)]
    [ProducesResponseType(typeof(ProblemDetails), StatusCodes.Status400BadRequest)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<LibroDto>> Actualizar(
    int id,
    ActualizarLibroDto request,
    CancellationToken cancellationToken)
    {
        var resultado = await _libroService.ActualizarAsync(
            id,
            request,
            cancellationToken);

        if (resultado.Estado == EstadoActualizarLibro.LibroNoEncontrado)
        {
            return NotFound();
        }

        if (resultado.Estado == EstadoActualizarLibro.GeneroNoEncontrado)
        {
            return BadRequest(new ProblemDetails
            {
                Status = StatusCodes.Status400BadRequest,
                Title = "Género inválido",
                Detail = "No existe un género con el GeneroId enviado."
            });
        }

        return Ok(resultado.Libro);
    }

    [HttpDelete("{id:int}")]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<IActionResult> Eliminar(
        int id,
        CancellationToken cancellationToken)
    {
        var eliminado = await _libroService.EliminarAsync(id, cancellationToken);

        if (!eliminado)
        {
            return NotFound();
        }

        return NoContent();
    }

}