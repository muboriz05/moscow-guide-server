package com.example.moscowguide.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val email = varchar("email", 128).uniqueIndex()
    val password = varchar("password", 128) // Added for auth
    val role = varchar("role", 20).default("USER")

    override val primaryKey = PrimaryKey(id)
}

object Places : Table("places") {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val shortDescription = text("short_description")
    val fullDescription = text("full_description")
    val address = varchar("address", 255)
    val metro = varchar("metro", 100)
    val workingHours = varchar("working_hours", 255)
    val rating = double("rating").default(0.0)
    val imageUrl = varchar("image_url", 512)
    val category = varchar("category", 100)
    val views = integer("views").default(0)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object Favorites : Table("favorites") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val placeId = integer("place_id").references(Places.id)

    override val primaryKey = PrimaryKey(id)
}
