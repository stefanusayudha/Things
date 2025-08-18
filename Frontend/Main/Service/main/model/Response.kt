package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response<T : Any>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: T?,
    @SerialName("error")
    val error: String?,
    @SerialName("message")
    val message: String?,
)
