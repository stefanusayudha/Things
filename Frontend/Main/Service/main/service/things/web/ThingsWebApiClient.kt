package service.things.web

import model.Response
import service.things.web.response.Thing

interface ThingsWebApiClient {
    suspend fun getListOfThings(): Result<List<Thing>>
}