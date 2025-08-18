package infra.kafka.events

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
sealed class ThingEvent {
    abstract val eventId: String
    abstract val thingId: String
    abstract val timestamp: Instant
    abstract val eventType: String
}

@Serializable
data class ThingCreatedEvent(
    override val eventId: String,
    override val thingId: String,
    override val timestamp: Instant,
    override val eventType: String = "ThingCreated",
    val name: String,
    val description: String?,
    val createdBy: String
) : ThingEvent()

@Serializable
data class ThingUpdatedEvent(
    override val eventId: String,
    override val thingId: String,
    override val timestamp: Instant,
    override val eventType: String = "ThingUpdated",
    val changes: Map<String, String>,
    val updatedBy: String
) : ThingEvent()

@Serializable
data class ThingDeletedEvent(
    override val eventId: String,
    override val thingId: String,
    override val timestamp: Instant,
    override val eventType: String = "ThingDeleted",
    val deletedBy: String
) : ThingEvent()

@Serializable
data class ThingStatusChangedEvent(
    override val eventId: String,
    override val thingId: String,
    override val timestamp: Instant,
    override val eventType: String = "ThingStatusChanged",
    val oldStatus: String,
    val newStatus: String,
    val changedBy: String
) : ThingEvent()
