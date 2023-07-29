package com.utsman.tokobola.common.component

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable

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

    LaunchedEffect(savedValue) {
        if (savedOffset > 0) {
            scrollState.scrollToItem(savedIndex, savedOffset)
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