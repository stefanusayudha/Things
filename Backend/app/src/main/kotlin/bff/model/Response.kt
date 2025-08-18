package bff.model

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response<T : Any>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: T?,
    @SerialName("error")
    val error: String?,
    @SerialName("message")
    val message: String?,
)

context(context: RoutingContext, route: Route)
suspend fun commonErrorHandling(e: Throwable) {
    return when (e) {
        is IllegalCallerException -> {
            illegalCaller(e.message ?: "Illegal request.")
        }

        is IllegalStateException, is BadRequestException, is IllegalArgumentException -> {
            badRequest(e.message ?: "Invalid request.")
        }

        else -> {
            internalServerError(e.message ?: "Internal server error.")
        }
    }
}

context(context: RoutingContext, route: Route)
suspend fun illegalCaller(error: String) {
    context.call.respond(
        status = HttpStatusCode.Forbidden,
        message = Response(
            success = false,
            data = null,
            error = error,
            message = "Shut it! Angry: Punk.",
        )
    )
}

context(context: RoutingContext, route: Route)
suspend fun badRequest(error: String) {
    context.call.respond(
        status = HttpStatusCode.BadRequest,
        message = Response(
            success = false,
            data = null,
            error = error,
            message = "Speak English! Sincere: Punk.",
        )
    )
}

context(context: RoutingContext, route: Route)
suspend fun internalServerError(error: String) {
    context.call.respond(
        status = HttpStatusCode.InternalServerError,
        message = Response(
            success = false,
            data = null,
            error = error,
            message = "It's your fault obviously. Sincere: Karen.",
        )
    )
}

context(context: RoutingContext, route: Route)
suspend fun notFound() {
    context.call.respond(
        status = HttpStatusCode.NotFound,
        message = Response(
            success = false,
            data = null,
            message = null,
            error = "There's nothing here, go away. Sincere: Punk.",
        )
    )
}

context(context: RoutingContext, route: Route)
suspend inline fun <reified T : Any> success(response: T?, message: String? = null) {
    context.call.respond(
        HttpStatusCode.OK,
        Response(
            success = true,
            message = message,
            data = response,
            error = null
        )
    )
}