using FrogsApi.Models;
using Microsoft.EntityFrameworkCore;

namespace FrogsApi.Data;

public class FrogsDbContext : DbContext
{
    public FrogsDbContext(DbContextOptions<FrogsDbContext> options)
        : base(options)
    {
    }

    public DbSet<Frog> Frogs => Set<Frog>();
}