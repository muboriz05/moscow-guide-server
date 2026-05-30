package com.example.moscowguide.routes

import com.example.moscowguide.repository.PlacesRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.placeRoutes(repository: PlacesRepository) {
    route("/api/places") {
        get {
            call.respond(repository.getAllPlaces())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }
            val place = repository.getPlaceById(id)
            if (place != null) {
                repository.incrementViews(id)
                call.respond(place)
            } else {
                call.respond(HttpStatusCode.NotFound, "Place not found")
            }
        }

        get("/search") {
            val query = call.request.queryParameters["q"] ?: ""
            call.respond(repository.searchPlaces(query))
        }

        get("/category/{name}") {
            val category = call.parameters["name"] ?: ""
            call.respond(repository.getPlacesByCategory(category))
        }
    }
}
