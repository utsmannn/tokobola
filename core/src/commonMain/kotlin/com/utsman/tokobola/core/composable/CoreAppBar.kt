package com.utsman.tokobola.core.composable

import androidx.compose.runtime.Composable

@Composable
expect fun CoreAppBar(
    title: String,
    previousTitle: String? = null,
    action: () -> Unit
)