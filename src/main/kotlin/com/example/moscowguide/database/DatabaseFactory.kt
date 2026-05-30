package com.example.moscowguide.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        // Using the EXACT host from the screenshot, including the .c-8 part
        val jdbcUrl = "jdbc:postgresql://ep-sweet-boat-aqsz4dk0-pooler.c-8.us-east-1.aws.neon.tech/neondb?sslmode=require"
        val user = "neondb_owner"
        val password = "npg_7HTdfVAKY5SJ"

        println("Connecting to database: $jdbcUrl with user: $user")

        val connectionPool = HikariDataSource(HikariConfig().apply {
            this.driverClassName = driverClassName
            this.jdbcUrl = jdbcUrl
            this.username = user
            this.password = password
            // Reduced pool size for initial testing
            this.maximumPoolSize = 2
            this.isReadOnly = false
            this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            this.validate()
        })

        val db = Database.connect(connectionPool)

        transaction(db) {
            SchemaUtils.create(Users, Places, Favorites)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
