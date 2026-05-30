package com.example.moscowguide.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)

@Serializable
data class Place(
    val id: Int? = null,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val address: String,
    val metro: String,
    val workingHours: String,
    val rating: Double = 0.0,
    val imageUrl: String,
    val category: String,
    val views: Int = 0,
    val createdAt: String? = null
)

@Serializable
data class Favorite(
    val id: Int,
    val userId: Int,
    val placeId: Int
)

@Serializable
data class Stats(
    val totalPlaces: Long,
    val totalUsers: Long,
    val totalViews: Long
)
