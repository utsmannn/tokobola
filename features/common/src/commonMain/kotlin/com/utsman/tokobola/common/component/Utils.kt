package com.utsman.tokobola.common.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.ignoreHorizontalParentPadding(horizontal: Dp): Modifier {
    return this.layout { measurable, constraints ->
        val overridenWidth = constraints.maxWidth + 2 * horizontal.roundToPx()
        val placeable = measurable.measure(constraints.copy(maxWidth = overridenWidth))
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

fun Modifier.ignoreVerticalParentPadding(vertical: Dp): Modifier {
    return this.layout { measurable, constraints ->
        val overridenHeight = constraints.maxHeight + 2 * vertical.roundToPx()
        val placeable = measurable.measure(constraints.copy(maxHeight = overridenHeight))
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

// (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -9) >= (lazyColumnListState.layoutInfo.totalItemsCount - 6)
fun LazyGridState.isScrolledToEnd(): Boolean {
    return (layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1)
    //return (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -9) >= (layoutInfo.totalItemsCount - 10)
}