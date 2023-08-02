package com.utsman.tokobola.explore.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class ExploreUiConfig(
    val offsetTabCategory: Float = 0f,
    val heightTabCategory: Int = 0,
    val selectedTabCategory: Int = 0
)

@Composable
fun rememberUiConfig(): ExploreUiConfig {
    return remember { ExploreUiConfig() }
}