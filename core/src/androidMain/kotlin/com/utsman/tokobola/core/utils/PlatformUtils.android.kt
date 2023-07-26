package com.utsman.tokobola.core.utils
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable

actual object PlatformUtils {

    @Composable
    actual fun rememberStatusBarHeight(): Int {
        val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
        return statusBarPadding.calculateTopPadding().value.toInt()
    }
}