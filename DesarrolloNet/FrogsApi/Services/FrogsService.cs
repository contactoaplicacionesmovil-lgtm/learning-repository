using FrogsApi.Contracts;
using FrogsApi.Data;
using FrogsApi.Models;
using Microsoft.EntityFrameworkCore;

namespace FrogsApi.Services;

public sealed class FrogsService : IFrogsService
{
private readonly FrogsDbContext _context;

    public FrogsService(FrogsDbContext context)
    {
        _context = context;
    }

    public async Task<FrogResponse> CreateAsync(
        CreateFrogRequest request,
        CancellationToken cancellationToken)
    {
        var frog = new Frog
        {
            Name = request.Name.Trim(),
            Type = request.Type.Trim(),
            Description = request.Description.Trim(),
            ImgSrc = request.ImgSrc.Trim()
        };

        _context.Frogs.Add(frog);
        await _context.SaveChangesAsync(cancellationToken);

        return ToResponse(frog);
    }

    public async Task<IReadOnlyList<FrogResponse>> GetAllAsync(
        CancellationToken cancellationToken)
    {
        var frogs = await _context.Frogs
            .AsNoTracking()
            .ToListAsync(cancellationToken);

        return frogs.Select(ToResponse).ToList();
    }

    public async Task<FrogResponse?> GetByIdAsync(
        int id,
        CancellationToken cancellationToken)
    {
        var frog = await _context.Frogs
            .AsNoTracking()
            .FirstOrDefaultAsync(f => f.Id == id, cancellationToken);

        return frog is null ? null : ToResponse(frog);
    }

    public async Task<IReadOnlyList<FrogResponse>> CreateManyAsync(
    IReadOnlyList<CreateFrogRequest> requests,
    CancellationToken cancellationToken)
{
    var frogs = requests.Select(request => new Frog
    {
        Name = request.Name.Trim(),
        Type = request.Type.Trim(),
        Description = request.Description.Trim(),
        ImgSrc = request.ImgSrc.Trim()
    }).ToList();

    _context.Frogs.AddRange(frogs);
    await _context.SaveChangesAsync(cancellationToken);

    return frogs.Select(ToResponse).ToList();
}

private static FrogResponse ToResponse(Frog frog) =>
        new(frog.Id, frog.Name, frog.Type, frog.Description, frog.ImgSrc);


}