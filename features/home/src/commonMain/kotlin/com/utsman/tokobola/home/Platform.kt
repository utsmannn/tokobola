package com.utsman.tokobola.home

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform