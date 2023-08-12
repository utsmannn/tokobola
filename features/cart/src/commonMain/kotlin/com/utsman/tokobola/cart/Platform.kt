package com.utsman.tokobola.cart

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform