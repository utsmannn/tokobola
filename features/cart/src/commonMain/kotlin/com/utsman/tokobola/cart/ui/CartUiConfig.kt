package com.utsman.tokobola.cart.ui

import com.utsman.tokobola.common.entity.Cart
import com.utsman.tokobola.core.utils.nowMillis

data class CartUiConfig(
    var carts: List<Cart> = emptyList(),
    val time: Long = nowMillis()
) {
    fun amount(): Double {
        return carts.sumOf { it.product.price * it.quantity }
    }

    fun isCartEmpty(): Boolean {
        return amount() <= 0.0
    }

    companion object {
        const val KEY_LOCATION_CURRENT = "current_location"
        const val KEY_LOCATION_SHIPPING = "shipping_location"
    }
}