package com.utaputranto.joyviekmp.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbErrorDto(
    @SerialName("status_code")
    val statusCode: Int?,
    @SerialName("status_message")
    val statusMessage: String?,
    @SerialName("success")
    val success: Boolean?,
)
