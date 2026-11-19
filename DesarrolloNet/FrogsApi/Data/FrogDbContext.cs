using FrogsApi.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace FrogsApi.Data;

public class FrogsDbContext : IdentityDbContext<ApplicationUser>
{
    public FrogsDbContext(DbContextOptions<FrogsDbContext> options)
        : base(options)
    {
    }

    public DbSet<Frog> Frogs => Set<Frog>();
}