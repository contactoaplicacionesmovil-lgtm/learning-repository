using System.Text.Json.Serialization;

namespace FrogsApi.Models;

public class Frog
{
    public int Id { get; set;}
    public string Name { get; set; } = string.Empty;
    public string Type { get; set; } = string.Empty;
    public string Description { get; set; } = string.Empty;

    [JsonPropertyName("img_src")]
    public string ImgSrc { get; set; } = string.Empty;
}