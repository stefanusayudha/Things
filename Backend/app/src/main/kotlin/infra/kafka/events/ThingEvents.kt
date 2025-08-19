package infra.kafka.events

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ThingEvent {
    @SerialName("eventId")
    abstract val eventId: String

    @SerialName("thingId")
    abstract val thingId: String

    @SerialName("timestamp")
    abstract val timestamp: Instant

    @SerialName("eventType")
    abstract val eventType: String
}

@Serializable
data class ThingCreatedEvent(
    @SerialName("eventId")
    override val eventId: String,
    @SerialName("thingId")
    override val thingId: String,
    @SerialName("timestamp")
    override val timestamp: Instant,
    @SerialName("eventType")
    override val eventType: String = "ThingCreated",
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String?,
    @SerialName("createdBy")
    val createdBy: String
) : ThingEvent()

@Serializable
data class ThingUpdatedEvent(
    @SerialName("eventId")
    override val eventId: String,
    @SerialName("thingId")
    override val thingId: String,
    @SerialName("timestamp")
    override val timestamp: Instant,
    @SerialName("eventType")
    override val eventType: String = "ThingUpdated",
    @SerialName("changes")
    val changes: Map<String, String>,
    @SerialName("updatedBy")
    val updatedBy: String
) : ThingEvent()

@Serializable
data class ThingDeletedEvent(
    @SerialName("eventId")
    override val eventId: String,
    @SerialName("thingId")
    override val thingId: String,
    @SerialName("timestamp")
    override val timestamp: Instant,
    @SerialName("eventType")
    override val eventType: String = "ThingDeleted",
    @SerialName("deletedBy")
    val deletedBy: String
) : ThingEvent()

@Serializable
data class ThingStatusChangedEvent(
    @SerialName("eventId")
    override val eventId: String,
    @SerialName("thingId")
    override val thingId: String,
    @SerialName("timestamp")
    override val timestamp: Instant,
    @SerialName("eventType")
    override val eventType: String = "ThingStatusChanged",
    @SerialName("oldStatus")
    val oldStatus: String,
    @SerialName("newStatus")
    val newStatus: String,
    @SerialName("changedBy")
    val changedBy: String
) : ThingEvent()
