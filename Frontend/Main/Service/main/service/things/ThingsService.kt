package service.things

import ProjectContext
import io.ktor.client.*
import service.things.web.KtorThingsWebApiClient
import service.things.web.ThingsWebApiClient
import service.things.web.response.Thing
import utils.defaultHttpClient

class ThingsService(
    private val webApi: ThingsWebApiClient
) {
    constructor(httpClient: HttpClient) : this(
        webApi = KtorThingsWebApiClient(httpClient)
    )

    constructor(context: ProjectContext) : this(
        httpClient = defaultHttpClient(context.webHostUrl)
    )

    suspend fun getListOfThings(): Result<List<Thing>> {
        return webApi.getListOfThings()
    }
}