package com.utsman.tokocot.api

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform