using FrogsApi.Contracts;

namespace FrogsApi.Services;

public interface IAuthService
{
    Task<LoginResponse?> LoginAsync(
        LoginRequest request,
        CancellationToken cancellationToken);
}