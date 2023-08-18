package com.utsman.tokobola.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.window.ComposeUIViewController

@Composable
actual fun NativeView(modifier: Modifier, content: @Composable () -> Unit) {
    UIKitView(
        factory = {
            ComposeUIViewController(content).view()
        },
        modifier = modifier
    )
}