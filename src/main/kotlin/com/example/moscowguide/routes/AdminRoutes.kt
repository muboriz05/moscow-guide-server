package com.example.moscowguide.routes

import com.example.moscowguide.models.Place
import com.example.moscowguide.models.Stats
import com.example.moscowguide.repository.PlacesRepository
import com.example.moscowguide.repository.UsersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import com.example.moscowguide.database.DatabaseFactory.dbQuery
import com.example.moscowguide.database.Places
import org.jetbrains.exposed.sql.sum

fun Route.adminRoutes(placesRepository: PlacesRepository, usersRepository: UsersRepository) {
    authenticate("auth-jwt") {
        route("/api/places") {
            post {
                if (!call.isAdmin()) return@post
                val place = call.receive<Place>()
                val added = placesRepository.addPlace(place)
                if (added != null) call.respond(HttpStatusCode.Created, added)
                else call.respond(HttpStatusCode.InternalServerError)
            }

            put("/{id}") {
                if (!call.isAdmin()) return@put
                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                val place = call.receive<Place>()
                if (placesRepository.updatePlace(id, place)) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.NotFound)
            }

            delete("/{id}") {
                if (!call.isAdmin()) return@delete
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (placesRepository.deletePlace(id)) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/api/admin/stats") {
            if (!call.isAdmin()) return@get
            val totalPlaces = placesRepository.getAllPlaces().size.toLong()
            val totalUsers = usersRepository.countUsers()
            val totalViews = dbQuery {
                Places.selectAll().sumOf { it[Places.views].toLong() }
            }
            call.respond(Stats(totalPlaces, totalUsers, totalViews))
        }
    }
}

suspend fun ApplicationCall.isAdmin(): Boolean {
    val principal = principal<JWTPrincipal>()
    val role = principal?.payload?.getClaim("role")?.asString()
    if (role != "ADMIN") {
        respond(HttpStatusCode.Forbidden, "Admin role required")
        return false
    }
    return true
}
