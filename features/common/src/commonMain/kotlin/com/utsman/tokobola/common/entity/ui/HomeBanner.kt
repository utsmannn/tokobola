package com.utsman.tokobola.common.entity.ui

import kotlinx.serialization.SerialName

data class HomeBanner(
    val id: Int = 0,
    val productId: Int = 0,
    val colorPrimary: String = "#FFFFFF",
    val colorAccent: String = "#000000",
    val productImage: String = "",
    val description: String = ""
)