package infra

import infra.things.ThingsDB
import infra.things.model.Thing
import infra.kafka.ThingsKafkaService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.slf4j.LoggerFactory

// fixme: kesalahan, infrastruktur tidak seharusnya memberikan interface untuk melakukan push message atau subscribe,
//  namun memberikan objek interface untuk digunakan client
object Infra {
    
    private val logger = LoggerFactory.getLogger(Infra::class.java)
    private val kafkaScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val thingsKafkaService = ThingsKafkaService()
    
    init {
        // Initialize Kafka consumers for handling events
        initializeKafkaConsumers()
    }
    
    suspend fun addNewThing(thing: Thing, createdBy: String = "system"): Result<Thing> {
        val thingsDB: ThingsDB = ThingsDB.Instance()
        return thingsDB.insert(thing).also { result ->
            if (result.isSuccess) {
                // Publish event when thing is successfully created
                thingsKafkaService.publishThingCreated(
                    thingId = thing.uuid,
                    name = thing.name,
                    description = "${thing.model} - ${thing.serialNumber}",
                    createdBy = createdBy
                ).onFailure { error ->
                    logger.error("Failed to publish ThingCreated event", error)
                }
            }
        }
    }

    suspend fun listOfThings(): Result<List<Thing>> {
        val thingsDB: ThingsDB = ThingsDB.Instance()
        return thingsDB.listOfThings()
    }
    
    suspend fun updateThing(thing: Thing, updatedBy: String = "system"): Result<Thing> {
        TODO()
//        val thingsDB: ThingsDB = ThingsDB.Instance()
//        return thingsDB.update(thing).also { result ->
//            if (result.isSuccess) {
//                // Publish event when thing is successfully updated
//                val changes = mapOf(
//                    "name" to thing.name,
//                    "model" to thing.model,
//                    "serialNumber" to thing.serialNumber
//                )
//                thingsKafkaService.publishThingUpdated(
//                    thingId = thing.uuid,
//                    changes = changes,
//                    updatedBy = updatedBy
//                ).onFailure { error ->
//                    logger.error("Failed to publish ThingUpdated event", error)
//                }
//            }
//        }
    }
    
    suspend fun deleteThing(thingId: String, deletedBy: String = "system"): Result<Boolean> {
//        val thingsDB: ThingsDB = ThingsDB.Instance()
//        return thingsDB.delete(thingId).also { result ->
//            if (result.isSuccess && result.getOrNull() == true) {
//                // Publish event when thing is successfully deleted
//                thingsKafkaService.publishThingDeleted(
//                    thingId = thingId,
//                    deletedBy = deletedBy
//                ).onFailure { error ->
//                    logger.error("Failed to publish ThingDeleted event", error)
//                }
//            }
//        }
        TODO()
    }
    
    private fun initializeKafkaConsumers() {
        // Start consuming events for logging/monitoring purposes
        thingsKafkaService.startEventConsumer(kafkaScope) { record ->
            logger.info("Received event: topic=${record.topic()}, key=${record.key()}, value=${record.value()}")
            // Add your event handling logic here
        }
        
        // Start consuming commands if you want to implement CQRS pattern
        thingsKafkaService.startCommandConsumer(kafkaScope) { record ->
            logger.info("Received command: topic=${record.topic()}, key=${record.key()}, value=${record.value()}")
            // Add your command handling logic here
        }
    }
    
    fun getKafkaService(): ThingsKafkaService = thingsKafkaService
    
    fun shutdown() {
        thingsKafkaService.close()
        logger.info("Infrastructure shutdown completed")
    }
}