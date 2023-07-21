package com.utsman.tokobola.core.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppText(modifier: Modifier = Modifier, text: String) {
    Text(text, modifier)
}