package bff.service.things

data class AddThingRequest(
    val name: String,
    val serialNumber: String,
    val model: String
)
