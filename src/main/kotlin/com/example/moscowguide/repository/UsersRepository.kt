package com.example.moscowguide.repository

import com.example.moscowguide.database.DatabaseFactory.dbQuery
import com.example.moscowguide.database.Users
import com.example.moscowguide.models.User
import org.jetbrains.exposed.sql.*

class UsersRepository {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name],
        email = row[Users.email],
        role = row[Users.role]
    )

    suspend fun findByEmail(email: String): User? = dbQuery {
        Users.selectAll().where { Users.email eq email }
            .map(::resultRowToUser)
            .singleOrNull()
    }
    
    suspend fun getPasswordByEmail(email: String): String? = dbQuery {
        Users.selectAll().where { Users.email eq email }
            .map { it[Users.password] }
            .singleOrNull()
    }

    suspend fun createUser(name: String, email: String, passwordHash: String, role: String = "USER"): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.name] = name
            it[Users.email] = email
            it[Users.password] = passwordHash
            it[Users.role] = role
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }
    
    suspend fun countUsers(): Long = dbQuery {
        Users.selectAll().count()
    }
}
