using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace FrogsApi.Contracts;

/// <summary> Datos necesarios para crear una rana.</summary>
public sealed record CreateFrogRequest
{
    [Required, StringLength(120)]
    public required string Name { get; init; }

    [Required, StringLength(80)]
    public required string Type { get; init; }

    [Required, StringLength(2000)]
    public required string Description { get; init; }

    [Required, Url, StringLength(2048)]
    [JsonPropertyName("img_src")]
    public required string ImgSrc { get; init; }
}