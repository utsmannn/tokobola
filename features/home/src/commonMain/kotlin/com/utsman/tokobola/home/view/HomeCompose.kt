package com.utsman.tokobola.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.composable.AppText
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.onFailure
import com.utsman.tokobola.core.onIdle
import com.utsman.tokobola.core.onLoading
import com.utsman.tokobola.core.onSuccess
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.home.HomeInstanceProvider
import com.utsman.tokobola.home.LocalHomeUseCase
import com.utsman.tokobola.home.entity.Product

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
            PullRefreshIndicator(
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
    LazyColumn {
        items(products.data) {
            Row(modifier = Modifier.padding(6.dp)) {
                val painter = rememberImagePainter(it.image[0])
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                AppText(modifier = Modifier.fillMaxWidth(), it.name)
            }
        }
    }
}

@Composable
fun HomeFailure(message: String) {
    AppText(text = message)
}

