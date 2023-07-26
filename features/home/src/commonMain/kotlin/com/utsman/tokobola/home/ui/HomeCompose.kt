package com.utsman.tokobola.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.utsman.tokobola.common.component.ProductItemGrid
import com.utsman.tokobola.common.component.ProductPullRefreshIndicator
import com.utsman.tokobola.common.entity.ui.Product
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.onFailure
import com.utsman.tokobola.core.onIdle
import com.utsman.tokobola.core.onLoading
import com.utsman.tokobola.core.onSuccess
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.PlatformUtils
import com.utsman.tokobola.home.LocalHomeUseCase

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home() {
    val homeUseCase = LocalHomeUseCase.current
    val homeViewModel = rememberViewModel { HomeViewModel(homeUseCase) }

    val products by homeViewModel.homeProduct.collectAsState()

    val isLoading by derivedStateOf {
        products is State.Loading
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            homeViewModel.getHomeProduct()
        }
    )

    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                with(products) {
                    onIdle {
                        homeViewModel.getHomeProduct()
                    }
                    onLoading {
                        HomeLoading()
                    }
                    onSuccess { productList ->
                        HomeSuccess(productList)
                    }
                    onFailure { throwable ->
                        HomeFailure(throwable.message.orEmpty())
                    }
                }
            }
            ProductPullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun HomeLoading() {

}

@Composable
fun HomeSuccess(products: Paged<Product>) {
    val navigation = LocalNavigation.current

    val statusBarHeight = PlatformUtils.rememberStatusBarHeight()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            top = statusBarHeight.dp,
            bottom = 6.dp,
            start = 6.dp,
            end = 6.dp
        )
    ) {
        items(products.data) { product ->
            ProductItemGrid(product) {
                navigation.goToDetail(it.id)
            }
        }
    }
}

@Composable
fun HomeFailure(message: String) {
    Text(text = message)
}

