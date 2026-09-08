# Moscow City Guide Backend

## Technologies
- **Kotlin** & **Ktor** (Server Framework)
- **PostgreSQL** (Database)
- **Exposed** (ORM)
- **HikariCP** (Connection Pool)
- **JWT** (Authentication)
- **Logback** (Logging)

## Project Structure
- `src/main/kotlin/com/example/moscowguide/`
    - `database/`: Database configuration and Exposed tables.
    - `models/`: Data classes for API responses.
    - `repository/`: Data access layer.
    - `routes/`: API endpoint definitions.
    - `plugins/`: Ktor feature configurations (Auth, Routing, Serialization).

## API Endpoints

### Public Endpoints
- `GET /health`: Health check.
- `POST /api/login`: Login to get JWT token.
- `POST /api/register`: Register a new user.
- `GET /api/places`: Get all places.
- `GET /api/places/{id}`: Get details of a specific place.
- `GET /api/places/search?q={query}`: Search places by title or description.
- `GET /api/places/category/{name}`: Filter places by category.

### Admin Endpoints (Require JWT & ADMIN role)
- `POST /api/places`: Add a new place.
- `PUT /api/places/{id}`: Update an existing place.
- `DELETE /api/places/{id}`: Delete a place.
- `GET /api/admin/stats`: Get dashboard statistics.

## Example Login
To access admin features, login with the default admin account:
- **Email**: `admin@moscowguide.ru`
- **Password**: `admin123`
