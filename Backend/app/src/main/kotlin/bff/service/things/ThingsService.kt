package bff.service.things

import bff.model.badRequest
import bff.model.commonErrorHandling
import bff.model.success
import infra.Infra
import infra.things.model.Thing
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.*

context(route: Route)
fun ThingsServiceBFF() {
    with(route) {
        route("/things") {
            post("/add") {
                addThing()
            }
        }
    }
}

context(context: RoutingContext, route: Route)
suspend fun addThing() {
    val request = context.call.receive<AddThingRequest>()

    require(request.model.isNotBlank()) {
        return badRequest("Missing required field: model.")
    }
    require(request.serialNumber.isNotBlank()) {
        return badRequest("Missing required field: serialNumber.")
    }

    Infra.addNewThing(
        Thing(
            uuid = UUID.randomUUID().toString(),
            name = request.name,
            serialNumber = request.serialNumber,
            model = request.model,
        )
    )
        .onFailure {
            return commonErrorHandling(it)
        }.onSuccess {
            return success(it, "")
        }
}