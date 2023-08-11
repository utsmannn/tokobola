package com.utsman.tokobola.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
expect fun rememberStatusBarHeightDp(): Dp

@Composable
expect fun rememberNavigationBarHeightDp(): Dp