# Moscow City Guide Backend

Backend API for the Moscow City Guide mobile application.

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

## Setup and Running

### 1. Database Setup (Neon.tech)
1. Create a project on [Neon.tech](https://neon.tech).
2. Get your connection string.
3. Use the `init.sql` file provided to seed your database with initial data (or tables will be created automatically on first run).

### 2. Environment Variables
Set the following environment variables:
- `DATABASE_URL`: Your PostgreSQL JDBC URL (e.g., `jdbc:postgresql://ep-example.eu-central-1.aws.neon.tech/neondb?sslmode=require`)
- `DATABASE_USER`: Database username.
- `DATABASE_PASSWORD`: Database password.
- `JWT_SECRET`: Secret key for JWT signing.

### 3. Run the Server
Using Gradle:
```bash
./gradlew :server:run
```
The server will start at `http://0.0.0.0:8080`.

## Example Login
To access admin features, login with the default admin account:
- **Email**: `admin@moscowguide.ru`
- **Password**: `admin123`
