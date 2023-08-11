package com.utsman.tokobola.core.utils
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
actual fun rememberStatusBarHeightDp(): Dp {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    return statusBarPadding.calculateTopPadding().value.toInt().dp
}

@Composable
actual fun rememberNavigationBarHeightDp(): Dp {
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues()
    return navBarPadding.calculateBottomPadding().value.toInt().dp
}