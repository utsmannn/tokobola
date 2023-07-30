package com.utsman.tokobola.common.component

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class KeyParams(
    val params: String = "",
    val index: Int,
    val scrollOffset: Int
)

private val SaveMap = mutableMapOf<String, KeyParams>()


@Composable
fun rememberForeverLazyListState(
    key: String,
    params: String = "",
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyGridState {
    var savedValue = SaveMap[key]
    if (savedValue?.params != params) savedValue = null
    val savedIndex = savedValue?.index ?: initialFirstVisibleItemIndex
    val savedOffset = savedValue?.scrollOffset ?: initialFirstVisibleItemScrollOffset
    val scrollState = rememberLazyGridState(savedIndex, savedOffset)

    val scope = rememberCoroutineScope()
    var scrollHasFix by remember { mutableStateOf(false) }

    scope.launch {
        if (savedOffset > 0 && !scrollHasFix) {
            scrollState.scrollToItem(savedIndex, savedOffset)
            delay(500)
            scrollHasFix = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            val lastIndex = scrollState.firstVisibleItemIndex
            val lastOffset = scrollState.firstVisibleItemScrollOffset
            SaveMap[key] = KeyParams(params, lastIndex, lastOffset)
        }
    }
    return scrollState
}


fun LazyGridState.isScrolledToEnd(): Boolean {
    return (layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1)
}

@Composable
fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
