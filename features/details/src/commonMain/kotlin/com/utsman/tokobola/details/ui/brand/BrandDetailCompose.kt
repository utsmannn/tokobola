package com.utsman.tokobola.details.ui.brand

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ProductItemGrid
import com.utsman.tokobola.common.component.ScaffoldGridState
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.TopBar
import com.utsman.tokobola.common.component.animatedTopBarColor
import com.utsman.tokobola.common.component.isScrolledToEnd
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.core.utils.rememberStatusBarHeightDp
import com.utsman.tokobola.details.LocalBrandDetailUseCase

@Composable
fun BrandDetail(brandId: Int) {
    val useCase = LocalBrandDetailUseCase.current
    val viewModel = rememberViewModel { BrandDetailViewModel(useCase) }

    val lazyGridState = rememberLazyGridState()

    val isReachBottom by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }

    val productListState by viewModel.productListState.collectAsState()
    val productList by viewModel.productListFlow.collectAsState()
    val brandTitle by viewModel.brandTitle.collectAsState()

    var isRetainData by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isRetainData = false
        viewModel.getProduct(brandId)
    }

    LaunchedEffect(isReachBottom) {
        if (productListState is State.Success && isReachBottom) {
            viewModel.getProduct(brandId)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (!isRetainData) {
                viewModel.clearData()
            }
        }
    }

    val topBarColor by lazyGridState.animatedTopBarColor

    ScaffoldGridState(
        topBar = {
            TopBar(
                text = brandTitle,
                modifier = Modifier
                    .background(color = topBarColor)
                    .wrapContentHeight()
                    .padding(
                        top = 12.dp + rememberStatusBarHeightDp(),
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 12.dp
                    ),
                lazyGridState = lazyGridState
            )
        },
        lazyGridState = lazyGridState,
        fixColumn = 2,
        modifier = Modifier.fillMaxSize(),
    ) {
        items(productList) {
            ProductItemGrid(it) {
                isRetainData = true
            }
        }

        with(productListState) {
            onLoading {
                items(listOf(1, 2)) {
                    Shimmer()
                }
            }
            onSuccess {
                viewModel.pushProductList(it.data)
            }
            onFailure {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    ErrorScreen(it)
                }
            }
        }
    }
}