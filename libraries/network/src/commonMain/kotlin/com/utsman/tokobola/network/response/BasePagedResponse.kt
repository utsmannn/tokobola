package com.utsman.tokobola.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasePagedResponse<T>(
    @SerialName("data")
    var `data`: DataResponse<T>?,
    @SerialName("message")
    var message: String?,
    @SerialName("status")
    var status: Boolean?
) {
    @Serializable
    data class DataResponse<T>(
        @SerialName("data")
        var `data`: List<T> = emptyList(),
        @SerialName("has_next_page")
        var hasNextPage: Boolean = false,
        @SerialName("page")
        var page: Int = 1,
        @SerialName("per_page")
        var perPage: Int = 10
    )
}