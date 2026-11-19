using System.Text.Json.Serialization;

namespace FrogsApi.Contracts;

/// <summary>Datos de una rana devueltos por la API.</summary>
public sealed record FrogResponse(
    int Id,
    string Name,
    string Type,
    string Description,
    [property: JsonPropertyName("img_src")] string ImgSrc);