package com.utsman.tokobola.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.utsman.tokobola.core.utils.PlatformUtils

@Composable
fun ScaffoldGridState(
    modifier: Modifier = Modifier.fillMaxSize(),
    lazyGridState: LazyGridState,
    fixColumn: Int = 2,
    topBar: @Composable BoxScope.() -> Unit = {},
    pullRefresh: @Composable BoxScope.() -> Unit = {},
    content: LazyGridScope.() -> Unit
) {

    val navigationBarHeight = PlatformUtils.rememberNavigationBarHeightDp()

    val isNeedLift by derivedStateOf {
        lazyGridState.canScrollBackward
    }

    val topModifier = if (isNeedLift) {
        Modifier.shadow(12.dp)
    } else {
        Modifier
    }

    Scaffold {
        Box(modifier = modifier) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(fixColumn),
                contentPadding = PaddingValues(
                    top = (Dimens.HeightTopBarSearch.value).dp,
                    bottom = (6 + (navigationBarHeight.value * 2)).dp,
                    start = 6.dp,
                    end = 6.dp
                ),
                state = lazyGridState,
                content = content,
            )

            Box(
                modifier = topModifier
            ) {
                topBar()
            }
            pullRefresh()
        }
    }
}