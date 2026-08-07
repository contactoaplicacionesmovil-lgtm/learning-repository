using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using UserManagementAPI2.Models;

namespace UserManagementAPI2.Controllers;

[ApiController]
[Route("api/[controller]")]
[Authorize]
public class UsersController : ControllerBase
{
    private static readonly List<User> Users =
    [
        new User
        {
            Id = 1,
            FirstName = "Ana",
            LastName = "García",
            Email = "ana.garcia@example.com",
            IsActive = true,
            CreatedAt = DateTime.UtcNow.AddDays(-10)
        },
        new User
        {
            Id = 2,
            FirstName = "Luis",
            LastName = "Martínez",
            Email = "luis.martinez@example.com",
            IsActive = true,
            CreatedAt = DateTime.UtcNow.AddDays(-7)
        },
        new User
        {
            Id = 3,
            FirstName = "Carla",
            LastName = "Ruiz",
            Email = "carla.ruiz@example.com",
            IsActive = false,
            CreatedAt = DateTime.UtcNow.AddDays(-3)
        }
    ];
    private static int _nextId = 4;
    private static readonly ReaderWriterLockSlim Sync = new();

    [HttpGet]
    public ActionResult<IEnumerable<User>> GetAll([FromQuery] int page = 1, [FromQuery] int pageSize = 20)
    {
        if (page < 1 || pageSize < 1 || pageSize > 100)
        {
            return BadRequestProblem("Parámetros de paginación inválidos. Usa page >= 1 y pageSize entre 1 y 100.");
        }

        Sync.EnterReadLock();
        try
        {
            var pagedUsers = Users
                .Skip((page - 1) * pageSize)
                .Take(pageSize)
                .ToList();

            return Ok(pagedUsers);
        }
        finally
        {
            Sync.ExitReadLock();
        }
    }

    [HttpGet("{id:int}")]
    public ActionResult<User> GetById(int id)
    {
        Sync.EnterReadLock();
        try
        {
            var user = Users.FirstOrDefault(u => u.Id == id);
            return user is null
                ? NotFoundProblem($"No se encontró un usuario con id {id}.")
                : Ok(user);
        }
        finally
        {
            Sync.ExitReadLock();
        }
    }

    [HttpPost]
    public ActionResult<User> Create([FromBody] CreateUserRequest request)
    {
        if (!ModelState.IsValid)
        {
            return ValidationProblemResponse();
        }

        var firstName = request.FirstName.Trim();
        var lastName = request.LastName.Trim();
        var email = request.Email.Trim().ToLowerInvariant();

        if (string.IsNullOrWhiteSpace(firstName) || string.IsNullOrWhiteSpace(lastName) || string.IsNullOrWhiteSpace(email))
        {
            if (string.IsNullOrWhiteSpace(firstName))
            {
                ModelState.AddModelError(nameof(request.FirstName), "El nombre es obligatorio.");
            }

            if (string.IsNullOrWhiteSpace(lastName))
            {
                ModelState.AddModelError(nameof(request.LastName), "El apellido es obligatorio.");
            }

            if (string.IsNullOrWhiteSpace(email))
            {
                ModelState.AddModelError(nameof(request.Email), "El email es obligatorio.");
            }

            return ValidationProblemResponse();
        }

        Sync.EnterWriteLock();
        try
        {
            var emailExists = Users.Any(u => u.Email.Equals(email, StringComparison.OrdinalIgnoreCase));
            if (emailExists)
            {
                return ConflictProblem("Ya existe un usuario con ese email.");
            }

            var user = new User
            {
                Id = _nextId++,
                FirstName = firstName,
                LastName = lastName,
                Email = email,
                IsActive = request.IsActive,
                CreatedAt = DateTime.UtcNow
            };

            Users.Add(user);
            return CreatedAtAction(nameof(GetById), new { id = user.Id }, user);
        }
        finally
        {
            Sync.ExitWriteLock();
        }
    }

    [HttpPut("{id:int}")]
    public ActionResult<User> Update(int id, [FromBody] UpdateUserRequest request)
    {
        if (!ModelState.IsValid)
        {
            return ValidationProblemResponse();
        }

        Sync.EnterWriteLock();
        try
        {
            var user = Users.FirstOrDefault(u => u.Id == id);
            if (user is null)
            {
                return NotFoundProblem($"No se encontró un usuario con id {id}.");
            }

            var emailInUse = Users.Any(u => u.Id != id && u.Email.Equals(request.Email, StringComparison.OrdinalIgnoreCase));
            if (emailInUse)
            {
                return ConflictProblem("Otro usuario ya tiene ese email.");
            }

            user.FirstName = request.FirstName;
            user.LastName = request.LastName;
            user.Email = request.Email;
            user.IsActive = request.IsActive;

            return Ok(user);
        }
        finally
        {
            Sync.ExitWriteLock();
        }
    }

    [HttpDelete("{id:int}")]
    public IActionResult Delete(int id)
    {
        Sync.EnterWriteLock();
        try
        {
            var user = Users.FirstOrDefault(u => u.Id == id);
            if (user is null)
            {
                return NotFoundProblem($"No se encontró un usuario con id {id}.");
            }

            Users.Remove(user);
            return NoContent();
        }
        finally
        {
            Sync.ExitWriteLock();
        }
    }

    private ActionResult ValidationProblemResponse()
    {
        var problem = new ValidationProblemDetails(ModelState)
        {
            Status = StatusCodes.Status400BadRequest,
            Title = "Error de validación.",
            Detail = "Uno o más datos de entrada no son válidos.",
            Instance = HttpContext.Request.Path
        };

        problem.Extensions["traceId"] = HttpContext.TraceIdentifier;

        return BadRequest(problem);
    }

    private ActionResult BadRequestProblem(string detail)
        => ProblemResponse(StatusCodes.Status400BadRequest, "Solicitud inválida.", detail);

    private ActionResult NotFoundProblem(string detail)
        => ProblemResponse(StatusCodes.Status404NotFound, "Recurso no encontrado.", detail);

    private ActionResult ConflictProblem(string detail)
        => ProblemResponse(StatusCodes.Status409Conflict, "Conflicto de negocio.", detail);

    private ActionResult ProblemResponse(int statusCode, string title, string detail)
    {
        var problem = new ProblemDetails
        {
            Status = statusCode,
            Title = title,
            Detail = detail,
            Instance = HttpContext.Request.Path
        };

        problem.Extensions["traceId"] = HttpContext.TraceIdentifier;

        return new ObjectResult(problem)
        {
            StatusCode = statusCode
        };
    }
}