package infra.things

import infra.things.model.Thing
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import utils.runCatching

class ThingsDBClient : ThingsDB {

    private val json = Json { prettyPrint = true }

    override suspend fun insert(thing: Thing): Result<Thing> {
        return runCatching(Dispatchers.IO) {
            newSuspendedTransaction(Dispatchers.IO) {
                val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

                // Check for existing records with explicit query first
                val existing = ThingsTable.selectAll()
                    .where {
                        (ThingsTable.uuid eq thing.uuid) or
                                (ThingsTable.serialNumber eq thing.serialNumber) or
                                (ThingsTable.name eq thing.name)
                    }
                    .limit(1).firstOrNull()

                check(existing == null) {
                    val duplicateField = when {
                        existing?.get(ThingsTable.uuid) == thing.uuid -> "uuid"
                        existing?.get(ThingsTable.serialNumber) == thing.serialNumber -> "serial_number"
                        existing?.get(ThingsTable.name) == thing.name -> "name"
                        else -> "unknown field"
                    }
                    throw IllegalArgumentException("Thing with duplicate $duplicateField already exists")
                }

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
