package com.utsman.tokobola.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun NativeView(modifier: Modifier, content: @Composable () -> Unit) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            ComposeView(context).apply {
                setContent { content.invoke() }
            }
        },
        modifier = modifier
    )
}