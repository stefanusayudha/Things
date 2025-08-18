package infra.things.model

data class Thing(
    val uuid: String,
    val name: String,
    val serialNumber: String,
    val model: String
)
