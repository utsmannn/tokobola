package com.utsman.tokobola.common.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.utsman.tokobola.core.utils.pxToDp
import com.utsman.tokobola.core.utils.rememberNavigationBarHeightDp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScaffoldGridState(
    modifier: Modifier = Modifier.fillMaxSize(),
    lazyGridState: LazyGridState,
    fixColumn: Int = 2,
    topBarPadding: Dp = Dimens.HeightTopBarSearch,
    topBar: @Composable BoxScope.() -> Unit = {},
    pullRefresh: @Composable BoxScope.() -> Unit = {},
    content: LazyGridScope.() -> Unit
) {

    val navigationBarHeight = rememberNavigationBarHeightDp()

    val isNeedLift by derivedStateOf {
        lazyGridState.canScrollBackward
    }

    val elevation by animateDpAsState(
        if (isNeedLift) 12.dp else 0.dp
    )

    Scaffold {
        BoxWithConstraints {

        }
        Box(modifier = modifier) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(fixColumn),
                contentPadding = PaddingValues(
                    top = topBarPadding + 12.dp,
                    bottom = (6 + (navigationBarHeight.value * 2)).dp,
                    start = 6.dp,
                    end = 6.dp
                ),
                state = lazyGridState,
                content = content,
            )

            Box(
                modifier = Modifier
                    .shadow(elevation)
            ) {
                topBar()
            }
            pullRefresh()
        }
    }
}