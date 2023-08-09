package com.utsman.tokobola.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

expect object PlatformUtils {

    @Composable
    fun rememberStatusBarHeightDp(): Dp

    @Composable
    fun rememberNavigationBarHeightDp(): Dp
}