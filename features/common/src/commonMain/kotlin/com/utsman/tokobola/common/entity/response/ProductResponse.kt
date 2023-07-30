package com.utsman.tokobola.common.entity.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("brand")
    var brand: BrandResponse?,
    @SerialName("category")
    var category: CategoryResponse?,
    @SerialName("description")
    var description: String?,
    @SerialName("id")
    var id: Int?,
    @SerialName("images")
    var images: List<String?>?,
    @SerialName("name")
    var name: String?,
    @SerialName("price")
    var price: Double?,
    @SerialName("promoted")
    var promoted: Boolean?
) {
    @Serializable
    data class BrandResponse(
        @SerialName("id")
        var id: Int?,
        @SerialName("name")
        var name: String?,
        @SerialName("logo")
        var logo: String?
    )

    @Serializable
    data class CategoryResponse(
        @SerialName("id")
        var id: Int?,
        @SerialName("name")
        var name: String?
    )
}