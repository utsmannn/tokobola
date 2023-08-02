package com.utsman.tokobola.home.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ProductItemGrid
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.PullRefreshIndicatorOffset
import com.utsman.tokobola.common.component.SearchBarStatic
import com.utsman.tokobola.common.component.SimpleErrorScreen
import com.utsman.tokobola.common.component.ignoreHorizontalParentPadding
import com.utsman.tokobola.common.component.ignoreVerticalParentPadding
import com.utsman.tokobola.common.component.isScrolledToEnd
import com.utsman.tokobola.common.component.isScrollingUp
import com.utsman.tokobola.common.component.rememberForeverLazyListState
import com.utsman.tokobola.common.component.shimmerBackground
import com.utsman.tokobola.common.entity.HomeBanner
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.PlatformUtils
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onIdle
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.core.utils.parseString
import com.utsman.tokobola.home.LocalHomeUseCase

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home() {
    val homeUseCase = LocalHomeUseCase.current
    val navigation = LocalNavigation.current

    val homeViewModel = rememberViewModel { HomeViewModel(homeUseCase) }

    val productsFeaturedState by homeViewModel.productsFeaturedState.collectAsState()
    val bannerState by homeViewModel.homeBannerState.collectAsState()
    val brandState by homeViewModel.brandState.collectAsState()

    val isLoading by derivedStateOf {
        bannerState is State.Loading
    }

    val navigationBarHeight = PlatformUtils.rememberNavigationBarHeight()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            homeViewModel.restartData()
        }
    )

    val lazyGridState = rememberForeverLazyListState("home_grid")

    val isReachBottom by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }

    val heightTopBar = animateDpAsState(
        targetValue = if (lazyGridState.isScrollingUp()) 0.dp else (-110).dp
    )

    // paging works
    LaunchedEffect(isReachBottom) {
        if (productsFeaturedState is State.Success && isReachBottom) {
            homeViewModel.getHomeProduct()
        }
    }

    val productList by homeViewModel.productsFeaturedFlow.collectAsState()
    val brandList by homeViewModel.brandListFlow.collectAsState()

    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {

            // main list
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                contentPadding = PaddingValues(
                    top = 6.dp,
                    bottom = (6 + (navigationBarHeight * 2)).dp,
                    start = 6.dp,
                    end = 6.dp
                ),
                state = lazyGridState
            ) {

                // banner state and item
                with(bannerState) {
                    onIdle {
                        homeViewModel.getHomeBanner()
                    }
                    onLoading {
                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(340.dp)
                                    .fillMaxWidth()
                                    .ignoreHorizontalParentPadding(6.dp)
                                    .ignoreVerticalParentPadding(6.dp)
                            ) {
                                Shimmer()
                            }
                        }
                    }
                    onSuccess {
                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(340.dp)
                                    .fillMaxWidth()
                                    .ignoreHorizontalParentPadding(6.dp)
                                    .ignoreVerticalParentPadding(6.dp)
                            ) {
                                Banner(it)
                            }
                        }
                    }
                    onFailure {
                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ) {
                            SimpleErrorScreen(it)
                        }
                    }
                }

                // brand item
                items(
                    items = brandList,
                    span = { GridItemSpan((this.maxLineSpan / 3)) }
                ) {
                    HomeBrandItem(it) {

                    }
                }

                // brand state
                with(brandState) {
                    onIdle {
                        homeViewModel.getBrand()
                    }
                    onLoading {
                        items(
                            items = listOf(1, 2, 3, 4, 5, 6),
                            span = { GridItemSpan((this.maxLineSpan / 3)) }
                        ) {
                            HomeBrandShimmer()
                        }
                    }
                    onSuccess {
                        homeViewModel.postBrandList(it)
                    }
                    onFailure {
                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ) {
                            ErrorScreen(it)
                        }
                    }
                }

                // divider title featured product
                item(
                    span = { GridItemSpan((this.maxLineSpan)) }
                ) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .height(30.dp)
                                .padding(6.dp)
                                .shimmerBackground()
                        )
                    } else {
                        Text(
                            text = "Featured Product",
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                // product item paging
                items(
                    items = productList,
                    span = { GridItemSpan(this.maxLineSpan / 2) }
                ) {
                    ProductItemGrid(it) { product ->
                        navigation.goToDetail(product.id)
                    }
                }

                // product state with paging
                with(productsFeaturedState) {
                    onIdle {
                        homeViewModel.getHomeProduct()
                    }
                    onLoading {
                        items(
                            items = listOf(1, 2),
                            span = { GridItemSpan(this.maxLineSpan / 2) }
                        ) {
                            Shimmer()
                        }
                    }
                    onSuccess {
                        homeViewModel.postProduct(it.data)
                    }
                    onFailure {
                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ) {
                            ErrorScreen(it)
                        }
                    }
                }
            }

            PullRefreshIndicatorOffset(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            // search bar
            SearchBarStatic(
                modifier = Modifier
                    .offset(y = heightTopBar.value)
                    .background(MaterialTheme.colors.primary)
            ) {

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(banner: List<HomeBanner>) {
    AutoSlidingCarousel(
        itemsCount = banner.size
    ) {
        val item = banner[it]
        val painter = rememberImagePainter(item.productImage)

        Box(
            modifier = Modifier.padding(bottom = 12.dp)
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

                Box(
                    modifier = Modifier
                        .background(
                            color = Color.parseString(item.colorAccent).copy(alpha = 0.3f)
                        ).align(Alignment.BottomStart)
                        .wrapContentSize()
                        .padding(12.dp)
                ) {

                    Text(
                        text = item.description,
                        fontSize = 23.sp,
                        maxLines = 3,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
