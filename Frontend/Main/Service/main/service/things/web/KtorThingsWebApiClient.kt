package service.things.web

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import service.things.web.response.Thing
import utils.ioDispatchers
import utils.runCatching

class KtorThingsWebApiClient(
    private val httpClient: HttpClient
) : ThingsWebApiClient {

    override suspend fun getListOfThings(): Result<List<Thing>> {
        return runCatching(ioDispatchers()) {
            val response = httpClient.get("things") {
                contentType(ContentType.Application.Json)
            }

            check(response.status.isSuccess()) {
                throw Exception("Failed to request OTP: ${response.status}")
            }

            response.body<List<Thing>>()
        }
    }
}

