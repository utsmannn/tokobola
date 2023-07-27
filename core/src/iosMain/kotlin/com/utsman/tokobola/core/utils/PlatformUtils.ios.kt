package com.utsman.tokobola.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication
import platform.UIKit.UIScreen

actual object PlatformUtils {

    @Composable
    actual fun rememberStatusBarHeight(): Int {
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
        }
    }


    @Composable
    actual fun rememberNavigationBarHeight(): Int {
        return remember {
            30
        }
    }
}