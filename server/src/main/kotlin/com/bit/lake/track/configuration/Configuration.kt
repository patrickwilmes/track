package com.bit.lake.track.configuration

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.Connection

@Configuration
class Configuration(
    @Value("\${database.host}")
    private val databaseHost: String,
    @Value("\${database.user}")
    private val databaseUser: String,
    @Value("\${database.pass}")
    private val databasePass: String,
) {

    @Bean
    fun configureDatabase() {
        Database.connect(
            "jdbc:postgresql://$databaseHost:5432/track", driver = "org.postgresql.Driver",
            user = databaseUser, password = databasePass
        )
        TransactionManager.manager.defaultIsolationLevel =
            Connection.TRANSACTION_SERIALIZABLE
    }

}