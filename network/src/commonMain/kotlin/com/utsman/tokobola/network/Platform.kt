package com.utsman.tokobola.network

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform