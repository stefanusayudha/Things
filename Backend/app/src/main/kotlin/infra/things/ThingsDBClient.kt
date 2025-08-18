package infra.things

import infra.things.model.Thing
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import utils.runCatching

class ThingsDBClient : ThingsDB {

    private val json = Json { prettyPrint = true }

    override suspend fun insert(thing: Thing): Result<Thing> {
        return runCatching(Dispatchers.IO) {
            newSuspendedTransaction(Dispatchers.IO) {
                val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

                ThingsTable.insert {
                    it[ThingsTable.uuid] = thing.uuid
                    it[ThingsTable.createdAt] = now
                    it[ThingsTable.updatedAt] = now
                    it[ThingsTable.name] = thing.name
                    it[ThingsTable.serialNumber] = thing.serialNumber
                    it[ThingsTable.model] = thing.model
                }

                thing
            }
        }
    }
}
