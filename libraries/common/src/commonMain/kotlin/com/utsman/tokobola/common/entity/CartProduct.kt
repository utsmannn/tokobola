package com.utsman.tokobola.common.entity

data class CartProduct(
    val productId: Int = 0,
    val quantity: Int = 0
) {

    fun isEmpty() = quantity == 0
}
