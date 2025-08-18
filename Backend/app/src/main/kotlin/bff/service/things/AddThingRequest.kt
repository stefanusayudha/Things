package bff.service.things

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddThingRequest(
    @SerialName("name")
    val name: String,
    @SerialName("serialNumber")
    val serialNumber: String,
    @SerialName("model")
    val model: String
)
