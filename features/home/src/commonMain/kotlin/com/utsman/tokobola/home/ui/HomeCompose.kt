package com.utsman.tokobola.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.ProductItemGrid
import com.utsman.tokobola.common.component.ProductPullRefreshIndicator
import com.utsman.tokobola.common.component.ignoreHorizontalParentPadding
import com.utsman.tokobola.common.component.ignoreVerticalParentPadding
import com.utsman.tokobola.common.component.isScrolledToEnd
import com.utsman.tokobola.common.entity.ui.HomeBanner
import com.utsman.tokobola.common.entity.ui.ThumbnailProduct
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onFailureComposed
import com.utsman.tokobola.core.utils.onIdle
import com.utsman.tokobola.core.utils.onIdleComposed
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onLoadingComposed
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.core.utils.onSuccessComposed
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.PlatformUtils
import com.utsman.tokobola.core.utils.merge
import com.utsman.tokobola.core.utils.parseString
import com.utsman.tokobola.home.LocalHomeUseCase

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home() {
    val homeUseCase = LocalHomeUseCase.current
    val navigation = LocalNavigation.current

    val homeViewModel = rememberViewModel { HomeViewModel(homeUseCase) }

    val products by homeViewModel.homeProduct.collectAsState()
    val banners by homeViewModel.homeBanner.collectAsState()

    val homeData by derivedStateOf {
        products.merge(banners) { data1, data2 ->
            Pair(data1, data2)
        }
    }

    val isLoading by derivedStateOf {
        homeData is State.Loading
    }

    val statusBarHeight = PlatformUtils.rememberStatusBarHeight()
    val navigationBarHeight = PlatformUtils.rememberNavigationBarHeight()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            homeViewModel.getHomeData()
        }
    )

    val lazyGridState = rememberLazyGridState()

    val isReachBottom by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }

    LaunchedEffect(isReachBottom) {
        if (products is State.Success && isReachBottom) {
            homeViewModel.getHomeProduct()
        }
        println("asuuuuuuuuuuu is bottom -> $isReachBottom")
    }

    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    top = 6.dp,
                    bottom = (6 + navigationBarHeight).dp,
                    start = 6.dp,
                    end = 6.dp
                ),
                state = lazyGridState
            ) {
                with(homeData) {
                    onIdle { homeViewModel.getHomeData() }
                    onLoading {  }
                    onSuccess { (productPage, banners) ->
                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(280.dp)
                                    .ignoreHorizontalParentPadding(6.dp)
                                    .ignoreVerticalParentPadding(6.dp)
                            ) {
                                BannerSuccess(banners)
                            }
                        }

                        items(productPage.data) { product ->
                            ProductItemGrid(product) {
                                navigation.goToDetail(it.id)
                            }
                        }
                    }
                    onFailure { throwable ->
                        item {
                            ProductListFailure(throwable.message.orEmpty())
                        }
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
fun ProductList(
    products: State<Paged<ThumbnailProduct>>,
    bannerContent: @Composable () -> Unit,
    onIdle: () -> Unit
) {
    with(products) {
        onIdleComposed { onIdle.invoke() }
        onLoadingComposed { ProductListLoading() }
        onSuccessComposed { ProductListSuccess(it, bannerContent) }
        onFailureComposed { ProductListFailure(it.message.orEmpty()) }
    }
}

@Composable
fun ProductListLoading() {

}

@Composable
fun ProductListSuccess(products: Paged<ThumbnailProduct>, bannerContent: @Composable () -> Unit) {
    val navigation = LocalNavigation.current

    val navigationBarHeight = PlatformUtils.rememberNavigationBarHeight()

    val lazyGridState = rememberLazyGridState()

    val isEndOfList by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }

    var currentPage by mutableStateOf(1)

    LaunchedEffect(isEndOfList) {
        if (currentPage == products.page) {
            currentPage = products.page+1
            println("asuuuuuuuuuuuuuu -> $currentPage | ${products.page}")
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            top = 6.dp,
            bottom = (6 + navigationBarHeight).dp,
            start = 6.dp,
            end = 6.dp
        ),
        state = lazyGridState
    ) {
        item(
            span = { GridItemSpan(this.maxLineSpan) }
        ) {
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .ignoreHorizontalParentPadding(6.dp)
                    .ignoreVerticalParentPadding(6.dp)
            ) { bannerContent.invoke() }
        }
        items(products.data) { product ->
            ProductItemGrid(product) {
                navigation.goToDetail(it.id)
            }
        }
    }
}

@Composable
fun ProductListFailure(message: String) {
    Text(text = message)
}


@Composable
fun Banner(banner: State<List<HomeBanner>>, onIdle: () -> Unit) {
    with(banner) {
        onIdleComposed { onIdle.invoke() }
        onLoadingComposed { BannerLoading() }
        onSuccessComposed { BannerSuccess(it) }
        onFailureComposed { BannerFailure(it.message.orEmpty()) }
    }
}

@Composable
fun BannerLoading() {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerSuccess(banner: List<HomeBanner>) {
    AutoSlidingCarousel(
        itemsCount = banner.size
    ) {
        val item = banner[it]
        val painter = rememberImagePainter(item.productImage)

        Box(
            modifier = Modifier.fillMaxSize().padding(bottom = 12.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = item.description,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier.background(
                    color = Color.parseString(item.colorPrimary).copy(alpha = 0.2f)
                )
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 30.dp)
                        .background(Color.parseString(item.colorAccent).copy(alpha = 0.3f))
                        .align(Alignment.BottomStart),
                    maxLines = 3,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun BannerFailure(message: String) {
    Text(text = message)
}
