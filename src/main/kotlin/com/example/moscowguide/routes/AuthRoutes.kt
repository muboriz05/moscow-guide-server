package com.example.moscowguide.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.moscowguide.repository.UsersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class AuthRequest(val email: String, val password: String)
@Serializable
data class RegisterRequest(val name: String, val email: String, val password: String, val role: String = "USER")

fun Route.authRoutes(usersRepository: UsersRepository) {
    post("/api/register") {
        val request = call.receive<RegisterRequest>()
        val existing = usersRepository.findByEmail(request.email)
        if (existing != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
            return@post
        }
        val user = usersRepository.createUser(request.name, request.email, request.password, request.role)
        if (user != null) {
            call.respond(HttpStatusCode.Created, user)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Could not create user")
        }
    }

    post("/api/login") {
        val request = call.receive<AuthRequest>()
        val password = usersRepository.getPasswordByEmail(request.email)
        if (password == request.password) { // Simple check for example
            val user = usersRepository.findByEmail(request.email)!!
            val token = generateToken(user.email, user.role)
            call.respond(mapOf("token" to token, "role" to user.role))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
        }
    }
}

fun generateToken(email: String, role: String): String {
    val jwtSecret = System.getenv("JWT_SECRET") ?: "secret"
    return JWT.create()
        .withAudience("moscowguide-users")
        .withIssuer("com.example.moscowguide")
        .withClaim("email", email)
        .withClaim("role", role)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000 * 24)) // 24 hours
        .sign(Algorithm.HMAC256(jwtSecret))
}
