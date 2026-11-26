using System.ComponentModel.DataAnnotations;

namespace FrogsApi.Contracts;

/// <summary>Credenciales para iniciar sesión.</summary>
public sealed record LoginRequest
{
    [Required, EmailAddress, StringLength(256)]
    public required string Email { get; init; }

    [Required, StringLength(256)]
    public required string Password { get; init; }
}