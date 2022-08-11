package com.bit.lake.trackserver.configuration

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

// todo - inject connection settings from properties or environment
fun Application.configureDatabase() =
    Database.connect(url = "jdbc:postgresql://localhost:5432/track", user = "postgres", password = "example")