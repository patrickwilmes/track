package com.bit.lake.trackserver.projects

import com.bit.lake.trackserver.projects.db.ProjectTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.IllegalArgumentException
import java.util.*

fun Application.installProjectRoutes() {
    routing {
        route("/projects") {
            get { }
            get("/{id}") {
            }
            post {
                val id = receiveProject()
                    .save {
                        Result.success(true)
                    }
                call.response.headers.append("Location", buildUrlForPath("/projects/$id"))
                call.respond(HttpStatusCode.Created)
            }
            delete("/{id}") {
            }
        }
    }
}

private fun PipelineContext<Unit, ApplicationCall>.buildUrlForPath(path: String) = "${assembleHostUrlPart()}$path"
private fun PipelineContext<Unit, ApplicationCall>.assembleHostUrlPart() = with(call.request.local) {
    "$scheme://$host:$port"
}

private fun Project.save(validate: (Project) -> Result<Boolean>): Result<String> {
    if (validate(this).isFailure) {
        return Result.failure(IllegalArgumentException("Poop"))
    }

    transaction {
        ProjectTable.insert {
            it[id] = "urn:projects:${UUID.randomUUID()}"
            it[name] = this.name
        }
    }
    return Result.success("")
}

private fun PipelineContext<Unit, ApplicationCall>.getId() = call.parameters["id"]
private suspend fun PipelineContext<Unit, ApplicationCall>.receiveProject() = call.receive<Project>()
