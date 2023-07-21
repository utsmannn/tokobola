package com.utsman.tokobola.details

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform