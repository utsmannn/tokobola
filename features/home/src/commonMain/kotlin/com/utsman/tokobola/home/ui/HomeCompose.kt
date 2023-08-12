package com.utsman.tokobola.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.Dimens
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ProductItemGrid
import com.utsman.tokobola.common.component.ProductItemGridRectangle
import com.utsman.tokobola.common.component.ScaffoldGridState
import com.utsman.tokobola.common.component.SearchBarStatic
import com.utsman.tokobola.common.component.SearchBarStaticWithTitle
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.SimpleErrorScreen
import com.utsman.tokobola.common.component.animatedTopBarColor
import com.utsman.tokobola.common.component.ignoreHorizontalParentPadding
import com.utsman.tokobola.common.component.ignoreVerticalParentPadding
import com.utsman.tokobola.common.component.isScrolledToEnd
import com.utsman.tokobola.common.component.rememberForeverLazyListState
import com.utsman.tokobola.common.component.shimmerBackground
import com.utsman.tokobola.common.component.tintDark
import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.HomeBanner
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onIdle
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.core.utils.parseString
import com.utsman.tokobola.core.utils.rememberStatusBarHeightDp
import com.utsman.tokobola.home.LocalHomeUseCase
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home() {
    val homeUseCase = LocalHomeUseCase.current

    val homeViewModel = rememberViewModel { HomeViewModel(homeUseCase) }

    val productsFeaturedState by homeViewModel.productsFeaturedState.collectAsState()
    val bannerState by homeViewModel.homeBannerState.collectAsState()
    val brandState by homeViewModel.brandState.collectAsState()
    val productViewedState by homeViewModel.productViewed.collectAsState()

    val isLoading by derivedStateOf {
        bannerState is State.Loading
    }

    val statusBarHeight = rememberStatusBarHeightDp()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
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

    // paging works
    LaunchedEffect(isReachBottom) {
        if (productsFeaturedState is State.Success && isReachBottom) {
            homeViewModel.getHomeProduct()
        }
    }

    val productList by homeViewModel.productsFeaturedFlow.collectAsState()
    val brandList by homeViewModel.brandListFlow.collectAsState()

    ScaffoldGridState(
        topBar = {
            SearchBarStaticWithTitle(
                lazyGridState = lazyGridState,
                title = "Welcome football lovers!"
            )
        },
        topBarPadding = Dimens.HeightTopBarSearchWithTitle,
        pullRefresh = {
            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
                    .offset(y = statusBarHeight)
            )
        },
        lazyGridState = lazyGridState,
        fixColumn = 6,
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        // banner state and item
        with(bannerState) {
            onIdle {
                homeViewModel.getHomeBanner()
            }
            onLoading {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 1.5f)
                    ) {
                        Shimmer()
                    }
                }
            }
            onSuccess {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(3f / 1.5f)
                            .ignoreHorizontalParentPadding(6.dp)
                            .ignoreVerticalParentPadding(6.dp)
                            .padding(bottom = 12.dp)
                    ) {
                        Banner(it)
                    }
                }
            }
            onFailure {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    SimpleErrorScreen(it)
                }
            }
        }

        // product viewed
        with(productViewedState) {
            onIdle {
                homeViewModel.getProductViewed()
            }
            onLoading {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Shimmer(
                        modifier = Modifier.fillMaxWidth()
                            .height(Dimens.HeightProductItemGridRectangle)
                    )
                }
            }
            onSuccess { products ->
                if (products.isNotEmpty()) {
                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Text(
                            text = "Recently Viewed",
                            modifier = Modifier.padding(6.dp),
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .ignoreHorizontalParentPadding(12.dp)
                    ) {
                        items(products) { product ->
                            ProductItemGridRectangle(product)
                        }
                    }
                }
            }
        }

        // divider title brand
        item(
            span = { GridItemSpan((maxLineSpan)) }
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
                    text = "Top Brands",
                    modifier = Modifier.padding(6.dp),
                    fontWeight = FontWeight.Black
                )
            }
        }

        // brand item
        items(
            items = brandList,
            span = { GridItemSpan((maxLineSpan / 3)) }
        ) {
            HomeBrandItem(it)
        }

        // brand state
        with(brandState) {
            onIdle {
                homeViewModel.getBrand()
            }
            onLoading {
                items(
                    items = listOf(1, 2, 3, 4, 5, 6),
                    span = { GridItemSpan((maxLineSpan / 3)) }
                ) {
                    HomeBrandShimmer()
                }
            }
            onSuccess {
                homeViewModel.postBrandList(it)
            }
            onFailure {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    ErrorScreen(it)
                }
            }
        }

        // divider title featured product
        item(
            span = { GridItemSpan((maxLineSpan)) }
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
                    modifier = Modifier.padding(6.dp),
                    fontWeight = FontWeight.Black
                )
            }
        }

        // product item paging
        items(
            items = productList,
            span = { GridItemSpan(maxLineSpan / 2) }
        ) {
            ProductItemGrid(it)
        }

        // product state with paging
        with(productsFeaturedState) {
            onIdle {
                homeViewModel.getHomeProduct()
            }
            onLoading {
                items(
                    items = listOf(1, 2),
                    span = { GridItemSpan(maxLineSpan / 2) }
                ) {
                    Shimmer()
                }
            }
            onSuccess {
                homeViewModel.postProduct(it.data)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(banner: List<HomeBanner>) {

    val pagerState = rememberPagerState()
    val navigation = LocalNavigation.current

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(5000L)
                val nextPage = (pagerState.currentPage + 1) % banner.count()
                pagerState.animateScrollToPage(
                    if (nextPage == banner.count() - 1) 0 else nextPage
                )
            }
        }

        val pageSize = remember {
            object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int,
                    pageSpacing: Int
                ): Int {
                    return availableSpace - (availableSpace / 6)
                }
            }
        }

        HorizontalPager(
            pageCount = banner.count(),
            state = pagerState,
            pageSpacing = 16.dp,
            flingBehavior = PagerDefaults.flingBehavior(
                pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(1)
            ),
            modifier = Modifier.align(Alignment.CenterStart),
            pageSize = pageSize,
            contentPadding = PaddingValues(12.dp)
        ) {
            val item = banner[it]
            val painter = rememberImagePainter(item.productImage)
            Box(
                modifier = Modifier.fillMaxHeight()
                    .fillMaxWidth()
                    .shadow(8.dp, clip = false, shape = RoundedCornerShape(Dimens.CornerSize))
                    .clip(RoundedCornerShape(Dimens.CornerSize))
                    .clickable {
                        navigation.goToDetailProduct(item.productId)
                    }
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tintDark(),
                    modifier = Modifier.fillMaxSize()
                )
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
                        fontSize = 18.sp,
                        maxLines = 3,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun HomeBrandItem(brand: Brand) {
    val navigation = LocalNavigation.current

    Column(
        modifier = Modifier
            .padding(6.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(Dimens.CornerSize))
            .background(color = MaterialTheme.colors.secondary.copy(alpha = 0.3f))
            .clickable {
                navigation.goToDetailBrand(brand.id)
            }
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter = rememberImagePainter(brand.logo)
        Image(
            painter = painter,
            contentDescription = brand.name,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = brand.name,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}

@Composable
fun HomeBrandShimmer() {

    Column(
        modifier = Modifier
            .padding(6.dp)
            .height(100.dp)
            .shimmerBackground()
    ) {}
}
