package com.utsman.tokobola.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform