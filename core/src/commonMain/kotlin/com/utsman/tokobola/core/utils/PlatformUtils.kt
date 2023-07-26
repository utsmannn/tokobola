package com.utsman.tokobola.core.utils

import androidx.compose.runtime.Composable

expect object PlatformUtils {

    @Composable
    fun rememberStatusBarHeight(): Int
}