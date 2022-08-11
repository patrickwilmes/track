package com.bit.lake.trackserver

import com.bit.lake.trackserver.configuration.configureDatabase
import com.bit.lake.trackserver.projects.db.ProjectTable
import com.bit.lake.trackserver.projects.installProjectRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowMethod(HttpMethod.Patch)
            allowHeader(HttpHeaders.Authorization)
            anyHost() // todo - remove in production
        }
        configureDatabase()
//        transaction {
//            SchemaUtils.create(ProjectTable)
//        }
        installProjectRoutes()
    }.start(wait = true)
}