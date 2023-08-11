package com.utsman.tokobola.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication

@Composable
actual fun rememberStatusBarHeightDp(): Dp {
    return remember {
        val currentHeight = UIApplication.sharedApplication.statusBarFrame.useContents {
            this.size.height
        }
        val statusBarHeight = if (currentHeight > 0) {
            currentHeight.toInt()
        } else {
            0
        }
        statusBarHeight
    }.dp
}


@Composable
actual fun rememberNavigationBarHeightDp(): Dp {
    return remember {
        30
    }.dp
}