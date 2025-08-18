package infra.kafka.events

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
sealed class ThingCommand {
    abstract val commandId: String
    abstract val thingId: String
    abstract val timestamp: Instant
    abstract val commandType: String
    abstract val requestedBy: String
}

@Serializable
data class CreateThingCommand(
    override val commandId: String,
    override val thingId: String,
    override val timestamp: Instant,
    override val commandType: String = "CreateThing",
    override val requestedBy: String,
    val name: String,
    val description: String?
) : ThingCommand()

@Serializable
data class UpdateThingCommand(
    override val commandId: String,
    override val thingId: String,
    override val timestamp: Instant,
    override val commandType: String = "UpdateThing",
    override val requestedBy: String,
    val changes: Map<String, String>
) : ThingCommand()

@Serializable
data class DeleteThingCommand(
    override val commandId: String,
    override val thingId: String,
    override val timestamp: Instant,
    override val commandType: String = "DeleteThing",
    override val requestedBy: String
) : ThingCommand()
