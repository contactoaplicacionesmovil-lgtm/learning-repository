namespace FrogsApi.Contracts;

/// <summary>Token de acceso y vencimiento.</summary>
public sealed record LoginResponse(string AccessToken, DateTimeOffset ExpiresAtUtc);