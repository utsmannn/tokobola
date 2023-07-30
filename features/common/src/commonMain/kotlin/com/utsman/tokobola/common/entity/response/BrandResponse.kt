package com.utsman.tokobola.common.entity.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("logo")
    val logo: String?
)