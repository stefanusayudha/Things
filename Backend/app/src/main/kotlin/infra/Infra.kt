package infra

import infra.things.ThingsDB
import infra.things.model.Thing

object Infra {
    suspend fun addNewThing(thing: Thing): Result<Thing> {
        val thingsDB: ThingsDB = ThingsDB.Instance()
        return thingsDB.insert(thing)
    }

    suspend fun listOfThings(): Result<List<Thing>> {
        val thingsDB: ThingsDB = ThingsDB.Instance()
        return thingsDB.listOfThings()
    }
}