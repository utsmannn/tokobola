package com.utsman.tokobola.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun NativeView(modifier: Modifier = Modifier, content: @Composable () -> Unit)