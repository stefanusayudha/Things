package service.things.web.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thing(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("name")
    val name: String,
    @SerialName("serialNumber")
    val serialNumber: String,
    @SerialName("model")
    val model: String
)