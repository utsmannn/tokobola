package com.utsman.tokobola.cart.ui

import com.utsman.tokobola.common.entity.Cart
import com.utsman.tokobola.core.utils.nowMillis

data class CartUiConfig(
    var carts: List<Cart> = emptyList(),
    val time: Long = nowMillis()
)