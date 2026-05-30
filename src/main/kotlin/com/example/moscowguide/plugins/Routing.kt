package com.example.moscowguide.plugins

import com.example.moscowguide.routes.*
import com.example.moscowguide.repository.PlacesRepository
import com.example.moscowguide.repository.UsersRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val placesRepository = PlacesRepository()
    val usersRepository = UsersRepository()

    routing {
        get("/health") {
            call.respondText("OK")
        }

        authRoutes(usersRepository)
        placeRoutes(placesRepository)
        adminRoutes(placesRepository, usersRepository)
    }
}
