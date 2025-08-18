package infra.kafka

import infra.config.KafkaConfig
import infra.kafka.events.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import java.util.*

class ThingsKafkaService(
    private val producer: KafkaProducerService = KafkaProducerService(),
    private val consumer: KafkaConsumerService = KafkaConsumerService()
) {
    
    private val logger = LoggerFactory.getLogger(ThingsKafkaService::class.java)
    
    // Event Publishing Methods
    suspend fun publishThingCreated(
        thingId: String,
        name: String,
        description: String?,
        createdBy: String
    ): Result<Unit> {
        val event = ThingCreatedEvent(
            eventId = UUID.randomUUID().toString(),
            thingId = thingId,
            timestamp = Clock.System.now(),
            name = name,
            description = description,
            createdBy = createdBy
        )
        
        return producer.sendMessage(
            topic = KafkaConfig.Topics.THINGS_EVENTS,
            key = thingId,
            message = event
        ).map { }
    }
    
    suspend fun publishThingUpdated(
        thingId: String,
        changes: Map<String, String>,
        updatedBy: String
    ): Result<Unit> {
        val event = ThingUpdatedEvent(
            eventId = UUID.randomUUID().toString(),
            thingId = thingId,
            timestamp = Clock.System.now(),
            changes = changes,
            updatedBy = updatedBy
        )
        
        return producer.sendMessage(
            topic = KafkaConfig.Topics.THINGS_EVENTS,
            key = thingId,
            message = event
        ).map { }
    }
    
    suspend fun publishThingDeleted(
        thingId: String,
        deletedBy: String
    ): Result<Unit> {
        val event = ThingDeletedEvent(
            eventId = UUID.randomUUID().toString(),
            thingId = thingId,
            timestamp = Clock.System.now(),
            deletedBy = deletedBy
        )
        
        return producer.sendMessage(
            topic = KafkaConfig.Topics.THINGS_EVENTS,
            key = thingId,
            message = event
        ).map { }
    }
    
    suspend fun publishThingStatusChanged(
        thingId: String,
        oldStatus: String,
        newStatus: String,
        changedBy: String
    ): Result<Unit> {
        val event = ThingStatusChangedEvent(
            eventId = UUID.randomUUID().toString(),
            thingId = thingId,
            timestamp = Clock.System.now(),
            oldStatus = oldStatus,
            newStatus = newStatus,
            changedBy = changedBy
        )
        
        return producer.sendMessage(
            topic = KafkaConfig.Topics.THINGS_EVENTS,
            key = thingId,
            message = event
        ).map { }
    }
    
    // Command Publishing Methods
    suspend fun publishCreateThingCommand(command: CreateThingCommand): Result<Unit> {
        return producer.sendMessage(
            topic = KafkaConfig.Topics.THINGS_COMMANDS,
            key = command.thingId,
            message = command
        ).map { }
    }
    
    suspend fun publishUpdateThingCommand(command: UpdateThingCommand): Result<Unit> {
        return producer.sendMessage(
            topic = KafkaConfig.Topics.THINGS_COMMANDS,
            key = command.thingId,
            message = command
        ).map { }
    }
    
    suspend fun publishDeleteThingCommand(command: DeleteThingCommand): Result<Unit> {
        return producer.sendMessage(
            topic = KafkaConfig.Topics.THINGS_COMMANDS,
            key = command.thingId,
            message = command
        ).map { }
    }
    
    // Consumer Methods
    fun startEventConsumer(
        scope: CoroutineScope,
        eventHandler: suspend (ConsumerRecord<String, String>) -> Unit
    ) {
        consumer.startConsuming(
            topics = listOf(KafkaConfig.Topics.THINGS_EVENTS),
            messageHandler = eventHandler,
            scope = scope
        )
        logger.info("Started consuming Thing events")
    }
    
    fun startCommandConsumer(
        scope: CoroutineScope,
        commandHandler: suspend (ConsumerRecord<String, String>) -> Unit
    ) {
        consumer.startConsuming(
            topics = listOf(KafkaConfig.Topics.THINGS_COMMANDS),
            messageHandler = commandHandler,
            scope = scope
        )
        logger.info("Started consuming Thing commands")
    }
    
    fun stopConsumers() {
        consumer.stopConsuming()
    }
    
    fun close() {
        producer.close()
        consumer.close()
    }
}
