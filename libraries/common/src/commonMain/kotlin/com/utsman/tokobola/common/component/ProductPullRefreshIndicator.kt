package com.utsman.tokobola.common.component

import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.utsman.tokobola.core.utils.rememberStatusBarHeightDp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshIndicatorOffset(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier
) {
    val offset = rememberStatusBarHeightDp()
    PullRefreshIndicator(
        refreshing, state, modifier.offset(y = offset)
    )
}