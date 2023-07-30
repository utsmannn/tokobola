package com.utsman.tokocot.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThumbnailProductResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("price")
    val price: Double?,
    @SerialName("brand")
    val brand: BrandResponse?,
    @SerialName("image")
    val image: String?,
    @SerialName("category")
    val category: CategoryResponse?,
    @SerialName("promoted")
    val promoted: Boolean?
) {
    @Serializable
    data class BrandResponse(
        @SerialName("id")
        val id: Int?,
        @SerialName("name")
        val name: String?,
        @SerialName("logo")
        val logo: String?
    )

    @Serializable
    data class CategoryResponse(
        @SerialName("id")
        val id: Int?,
        @SerialName("name")
        val name: String?
    )

}