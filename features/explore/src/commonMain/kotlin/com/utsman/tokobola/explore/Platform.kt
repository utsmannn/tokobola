package com.utsman.tokobola.explore

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform