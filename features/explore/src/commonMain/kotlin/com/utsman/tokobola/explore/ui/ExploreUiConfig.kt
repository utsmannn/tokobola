package com.utsman.tokobola.explore.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.utsman.tokobola.common.entity.Category

data class ExploreUiConfig(
    val offsetTabCategory: Float = 0f,
    val heightTabCategory: Int = 0,
    val selectedTabCategory: Int = 0,
    val selectedCategory: Category = Category()
)

@Composable
fun rememberUiConfig(): ExploreUiConfig {
    return remember { ExploreUiConfig() }
}