package com.utsman.tokobola.common.entity

data class Cart(
    val product: ThumbnailProduct = ThumbnailProduct(),
    var quantity: Int = 0,
    var millis: Long
)