@file:OptIn(ExperimentalSerializationApi::class)

package com.utsman.tokobola.network.response


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("data")
    var `data`: T?,
    @SerialName("message")
    var message: String?,
    @SerialName("status")
    var status: Boolean?
)