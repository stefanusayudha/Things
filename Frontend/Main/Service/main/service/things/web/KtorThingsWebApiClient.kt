package service.things.web

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import model.Response
import service.things.web.response.Thing
import utils.ioDispatchers
import utils.runCatching

class KtorThingsWebApiClient(
    private val httpClient: HttpClient
) : ThingsWebApiClient {

    override suspend fun getListOfThings(): Result<List<Thing>> {
        return runCatching(ioDispatchers()) {
            val raw = httpClient.get("things") {
                contentType(ContentType.Application.Json)
            }

            check(raw.status.isSuccess()) {
                throw Exception("Failed to request OTP: ${raw.status}")
            }

            val response = raw.body<Response<List<Thing>>>()

            require(response.success) {
                // fixme: map exception properly
                throw Exception(response.error ?: "Unknown Error")
            }

            response.data.orEmpty()
        }
    }
}

