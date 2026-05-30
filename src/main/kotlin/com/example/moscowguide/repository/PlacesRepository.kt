package com.example.moscowguide.repository

import com.example.moscowguide.database.DatabaseFactory.dbQuery
import com.example.moscowguide.database.Places
import com.example.moscowguide.models.Place
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PlacesRepository {
    private fun resultRowToPlace(row: ResultRow) = Place(
        id = row[Places.id],
        title = row[Places.title],
        shortDescription = row[Places.shortDescription],
        fullDescription = row[Places.fullDescription],
        address = row[Places.address],
        metro = row[Places.metro],
        workingHours = row[Places.workingHours],
        rating = row[Places.rating],
        imageUrl = row[Places.imageUrl],
        category = row[Places.category],
        views = row[Places.views],
        createdAt = row[Places.createdAt].toString()
    )

    suspend fun getAllPlaces(): List<Place> = dbQuery {
        Places.selectAll().map(::resultRowToPlace)
    }

    suspend fun getPlaceById(id: Int): Place? = dbQuery {
        Places.selectAll().where { Places.id eq id }
            .map(::resultRowToPlace)
            .singleOrNull()
    }

    suspend fun searchPlaces(query: String): List<Place> = dbQuery {
        val lowerQuery = "%${query.lowercase()}%"
        Places.selectAll().where { 
            (Places.title.lowerCase() like lowerQuery) or (Places.shortDescription.lowerCase() like lowerQuery)
        }.map(::resultRowToPlace)
    }

    suspend fun getPlacesByCategory(category: String): List<Place> = dbQuery {
        Places.selectAll().where { Places.category eq category }
            .map(::resultRowToPlace)
    }

    suspend fun addPlace(place: Place): Place? = dbQuery {
        val insertStatement = Places.insert {
            it[title] = place.title
            it[shortDescription] = place.shortDescription
            it[fullDescription] = place.fullDescription
            it[address] = place.address
            it[metro] = place.metro
            it[workingHours] = place.workingHours
            it[rating] = place.rating
            it[imageUrl] = place.imageUrl
            it[category] = place.category
            it[views] = 0
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPlace)
    }

    suspend fun updatePlace(id: Int, place: Place): Boolean = dbQuery {
        Places.update({ Places.id eq id }) {
            it[title] = place.title
            it[shortDescription] = place.shortDescription
            it[fullDescription] = place.fullDescription
            it[address] = place.address
            it[metro] = place.metro
            it[workingHours] = place.workingHours
            it[imageUrl] = place.imageUrl
            it[category] = place.category
        } > 0
    }

    suspend fun deletePlace(id: Int): Boolean = dbQuery {
        Places.deleteWhere { Places.id eq id } > 0
    }
    
    suspend fun incrementViews(id: Int) = dbQuery {
        Places.update({ Places.id eq id }) {
            with(SqlExpressionBuilder) {
                it[views] = views + 1
            }
        }
    }
}
