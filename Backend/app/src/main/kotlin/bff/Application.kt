package bff

import bff.model.notFound
import bff.service.things.ThingsServiceBFF
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = HOST, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // fixme: handle origin properly
    install(CORS) {
        anyHost() // ⚠️ allows all hosts — use carefully in production
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
    }

    // Configure JSON serialization
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    routing {
        ThingsServiceBFF()

        // Default 404 handler for any unmatched routes
        route("{...}") { handle { notFound() } }
    }
}
