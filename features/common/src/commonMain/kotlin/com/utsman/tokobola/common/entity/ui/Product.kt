package com.utsman.tokobola.common.entity.ui

data class Product(
    val category: String = "",
    val description: String = "",
    val id: Int = 0,
    val images: List<String> = emptyList(),
    val name: String = "",
    val price: Double = 0.0,
    val isPromoted: Boolean = false,
    val brand: ThumbnailProduct.ThumbnailBrand = ThumbnailProduct.ThumbnailBrand()
)