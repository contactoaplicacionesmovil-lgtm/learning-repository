using Biblioteca.Api.DTOs;
using Biblioteca.Api.Services;
using Microsoft.AspNetCore.Mvc;

namespace Biblioteca.Api.Controllers;

[ApiController]
[Route("api/generos")]
public sealed class GenerosController : ControllerBase
{
    private readonly IGeneroService _generoService;

    public GenerosController(IGeneroService generoService)
    {
        _generoService = generoService;
    }

    [HttpGet]
    [ProducesResponseType(typeof(IReadOnlyList<GeneroDto>), StatusCodes.Status200OK)]
    public async Task<ActionResult<IReadOnlyList<GeneroDto>>> ObtenerTodos(
        CancellationToken cancellationToken)
    {
        var generos = await _generoService.ObtenerTodosAsync(cancellationToken);
        return Ok(generos);
    }

    [HttpGet("{id:int}")]
    [ProducesResponseType(typeof(GeneroDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<GeneroDto>> ObtenerPorId(
        int id,
        CancellationToken cancellationToken)
    {
        var genero = await _generoService.ObtenerPorIdAsync(id, cancellationToken);

        if (genero is null)
        {
            return NotFound();
        }

        return Ok(genero);
    }

    [HttpPost]
    [ProducesResponseType(typeof(GeneroDto), StatusCodes.Status201Created)]
    [ProducesResponseType(typeof(ProblemDetails), StatusCodes.Status409Conflict)]
    public async Task<ActionResult<GeneroDto>> Crear(
        CrearGeneroDto request,
        CancellationToken cancellationToken)
    {
        var genero = await _generoService.CrearAsync(request, cancellationToken);

        if (genero is null)
        {
            return Conflict(new ProblemDetails
            {
                Status = StatusCodes.Status409Conflict,
                Title = "Género duplicado",
                Detail = "Ya existe un género con ese nombre."
            });
        }

        return CreatedAtAction(
            nameof(ObtenerPorId),
            new { id = genero.Id },
            genero);
    }
}