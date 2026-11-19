using FrogsApi.Contracts;

namespace FrogsApi.Services;

public interface IFrogsService
{
    Task<IReadOnlyList<FrogResponse>> GetAllAsync(CancellationToken cancellationToken);
    Task<FrogResponse?> GetByIdAsync(int id, CancellationToken cancellationToken);
    Task<FrogResponse> CreateAsync(
        CreateFrogRequest request,
        CancellationToken cancellationToken);

        Task<IReadOnlyList<FrogResponse>> CreateManyAsync(
    IReadOnlyList<CreateFrogRequest> requests,
    CancellationToken cancellationToken);
}