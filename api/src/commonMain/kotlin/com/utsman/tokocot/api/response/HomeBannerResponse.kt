package com.utsman.tokocot.api.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeBannerResponse(
    @SerialName("product_id")
    val productId: Int?,
    @SerialName("color_primary")
    val colorPrimary: String?,
    @SerialName("color_accent")
    val colorAccent: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("product_image")
    val productImage: String?,
    @SerialName("description")
    val description: String?
)