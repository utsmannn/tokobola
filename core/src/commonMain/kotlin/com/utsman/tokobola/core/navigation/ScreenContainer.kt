package com.utsman.tokobola.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey

interface ScreenContainer {

    fun home(): Screen
    fun detail(productId: Int): Screen
}

val LocalScreenContainer = compositionLocalOf<ScreenContainer> { error("screen container not found") }

fun screenContentOf(key: String? = null, content: @Composable () -> Unit): Screen {
    return object : Screen {
        override val key: ScreenKey = key ?: super.key

        @Composable
        override fun Content() {
            content.invoke()
        }
    }
}