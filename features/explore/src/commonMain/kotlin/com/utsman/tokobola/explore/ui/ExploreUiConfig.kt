package com.utsman.tokobola.explore.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class ExploreUiConfig(
    var offsetTabCategory: Float = 0f,
    var heightTabCategory: Int = 0,
    var selectedTabCategory: Int = 0
)

@Composable
fun rememberUiConfig(): ExploreUiConfig {
    return remember { ExploreUiConfig() }
}