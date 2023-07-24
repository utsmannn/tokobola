package com.utsman.tokobola.home.entity

data class Product(
    val category: String = "",
    val description: String = "",
    val id: Int = 0,
    val image: List<String> = emptyList(),
    val name: String = "",
    val price: Double = 0.0,
    val isPromoted: Boolean = false,
    val brand: String = ""
)