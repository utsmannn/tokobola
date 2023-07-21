package com.utsman.tokobola.details.entity

data class ProductDetail(
    val category: String = "",
    val description: String = "",
    val id: Int = 0,
    val image: List<String> = emptyList(),
    val name: String = "",
    val price: Double = 0.0,
    val promoted: Boolean = false
)
