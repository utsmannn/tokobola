package com.utsman.tokobola.details.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailResponse(
    @SerialName("category")
    var category: String?,
    @SerialName("description")
    var description: String?,
    @SerialName("id")
    var id: Int?,
    @SerialName("image")
    var image: List<String?>?,
    @SerialName("name")
    var name: String?,
    @SerialName("price")
    var price: Double?,
    @SerialName("promoted")
    var promoted: Boolean?
)