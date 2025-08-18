package infra.things

import infra.config.DatabaseConnection
import infra.things.model.Thing
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import utils.runCatching

interface ThingsDB {
    companion object Companion {
        private var instance: ThingsDB? = null

        suspend fun Instance(): ThingsDB {
            if (instance == null) {
                val client = ThingsDBClient()
                client.initializeDatabase().getOrThrow()
                instance = client
            }
            return instance!!
        }

        private var testInstance: ThingsDB? = null

        suspend fun TestInstance(): ThingsDB {
            if (testInstance == null) {
                val client = ThingsDBClient()
                client.initializeTestingDatabase().getOrThrow()
                testInstance = client
            }
            return testInstance!!
        }
    }

    suspend fun initializeDatabase(): Result<Unit> {
        return runCatching(Dispatchers.IO) {
            DatabaseConnection.connection()
            newSuspendedTransaction {
                SchemaUtils.create(ThingsTable)
            }
        }
    }

    suspend fun initializeTestingDatabase(): Result<Unit> {
        return runCatching(Dispatchers.IO) {
            DatabaseConnection.testConnection()
            newSuspendedTransaction {
                SchemaUtils.create(ThingsTable)
            }
        }
    }

    suspend fun insert(thing: Thing): Result<Thing>
}