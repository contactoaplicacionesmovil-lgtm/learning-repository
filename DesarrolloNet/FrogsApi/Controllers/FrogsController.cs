using FrogsApi.Contracts;
using FrogsApi.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;

namespace FrogsApi.Controllers;

[Authorize]
[ApiController]
[Route("api/[controller]")]
public class FrogsController : ControllerBase
{
    private readonly IFrogsService _frogsService;


    public FrogsController(IFrogsService frogsService)
    {
        _frogsService = frogsService;
    }


    [HttpGet]
    public async Task<ActionResult<IReadOnlyList<FrogResponse>>> Get(
        CancellationToken cancellationToken)
    {
        return Ok(await _frogsService.GetAllAsync(cancellationToken));
    }


        [HttpGet("{id:int}")]
    public async Task<ActionResult<FrogResponse>> GetById(
        int id,
        CancellationToken cancellationToken)
    {
        var frog = await _frogsService.GetByIdAsync(id, cancellationToken);
        return frog is null ? NotFound() : Ok(frog);
    }

        [HttpPost]
    [ProducesResponseType(typeof(FrogResponse), StatusCodes.Status201Created)]
    public async Task<ActionResult<FrogResponse>> Create(
        CreateFrogRequest request,
        CancellationToken cancellationToken)
    {
        var frog = await _frogsService.CreateAsync(request, cancellationToken);

        return CreatedAtAction(nameof(GetById), new { id = frog.Id }, frog);
    }

[HttpPost("bulk")]
[ProducesResponseType(typeof(IReadOnlyList<FrogResponse>), StatusCodes.Status201Created)]
public async Task<ActionResult<IReadOnlyList<FrogResponse>>> CreateMany(
    [FromBody] List<CreateFrogRequest> requests,
    CancellationToken cancellationToken)
{
    if (requests.Count == 0)
    {
        return BadRequest("Debes enviar al menos una rana.");
    }

    var frogs = await _frogsService.CreateManyAsync(requests, cancellationToken);

    return CreatedAtAction(nameof(Get), routeValues: null, value: frogs);
}

}