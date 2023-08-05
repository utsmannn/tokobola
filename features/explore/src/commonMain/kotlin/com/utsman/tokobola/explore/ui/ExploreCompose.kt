package com.utsman.tokobola.explore.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.Dimens
import com.utsman.tokobola.common.component.ProductItemGridRectangle
import com.utsman.tokobola.common.component.SearchBarStatic
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.ignoreHorizontalParentPadding
import com.utsman.tokobola.common.component.ignoreVerticalParentPadding
import com.utsman.tokobola.common.component.tintDark
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.PlatformUtils
import com.utsman.tokobola.core.utils.onIdle
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.explore.LocalExploreUseCase
import kotlinx.coroutines.delay


@Composable
fun Explore() {
    val exploreUseCase = LocalExploreUseCase.current

    val exploreViewModel = rememberViewModel { ExploreViewModel(exploreUseCase) }

    val navigationBarHeight = PlatformUtils.rememberNavigationBarHeight()

    val topProductState by exploreViewModel.topProductState.collectAsState()
    val curatedProductState by exploreViewModel.curatedProductState.collectAsState()

    val categoryState by exploreViewModel.categoryState.collectAsState()
    val brandState by exploreViewModel.brandState.collectAsState()

    val productCategoryState by exploreViewModel.productCategoryState.collectAsState()
    val productBrandState by exploreViewModel.productBrandState.collectAsState()

    val uiConfig by exploreViewModel.uiConfig.collectAsState()

    val lazyListState = rememberLazyListState()

    val isColorizeSearchBar by remember {
        derivedStateOf { !lazyListState.canScrollBackward }
    }

    val searchBarColor by animateColorAsState(
        targetValue = if (isColorizeSearchBar) Color.Transparent else MaterialTheme.colors.primary,
        animationSpec = tween(durationMillis = 300)
    )

    // val isAllowClick = productCategoryState is State.Success

    val isCategoryTabAllowClick by remember {
        derivedStateOf {
            productCategoryState is State.Success
        }
    }

    val isBrandTabAllowClick by remember {
        derivedStateOf {
            productBrandState is State.Success
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    top = (Dimens.HeightTopBarSearch.value).dp,
                    bottom = (6 + (navigationBarHeight * 2)).dp,
                    start = 6.dp,
                    end = 6.dp
                ),
                state = lazyListState
            ) {

                // top product
                with(topProductState) {
                    onIdle {
                        exploreViewModel.getTopProduct()
                    }
                    onLoading {
                        item {
                            Shimmer(modifier = Modifier.height(120.dp))
                        }
                    }
                    onSuccess {
                        item {
                            Text(
                                text = "Hot products",
                                modifier = Modifier.padding(6.dp),
                                fontWeight = FontWeight.Black
                            )
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(3f / 1.5f)
                                    .ignoreHorizontalParentPadding(6.dp)
                                    .ignoreVerticalParentPadding(6.dp)
                            ) {
                                TopProductBanner(it)
                            }
                        }
                    }
                }

                // curated product
                with(curatedProductState) {
                    onIdle {
                        exploreViewModel.getCuratedProduct()
                    }
                    onLoading {
                        item {
                            Shimmer(modifier = Modifier.height(120.dp))
                        }
                    }
                    onSuccess {
                        item {
                            Text(
                                text = "For you",
                                modifier = Modifier.padding(6.dp).padding(top = 12.dp),
                                fontWeight = FontWeight.Black
                            )
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(3f / 1.5f)
                                    .ignoreHorizontalParentPadding(6.dp)
                                    .ignoreVerticalParentPadding(6.dp)
                            ) {
                                TopProductBanner(it)
                            }
                        }
                    }
                }

                // category tab
                with(categoryState) {
                    onIdle {
                        exploreViewModel.getCategory()
                    }
                    onLoading {
                        item {
                            Shimmer(modifier = Modifier.height(100.dp))
                        }
                    }
                    onSuccess { categories ->
                        item {
                            Text(
                                text = "Categories",
                                modifier = Modifier.padding(6.dp).padding(top = 12.dp),
                                fontWeight = FontWeight.Black
                            )
                        }
                        item {
                            ScrollableTabRow(
                                selectedTabIndex = uiConfig.selectedTabCategoryIndex,
                                modifier = Modifier.fillMaxWidth()
                                    .ignoreHorizontalParentPadding(12.dp),
                                backgroundColor = Color.Transparent,
                                edgePadding = 12.dp,
                                indicator = {},
                                divider = {}
                            ) {
                                categories.forEachIndexed { index, category ->
                                    val isSelected by remember {
                                        derivedStateOf {
                                            index == uiConfig.selectedTabCategoryIndex
                                        }
                                    }

                                    val selectedTabColor by animateColorAsState(
                                        targetValue = if (isSelected) MaterialTheme.colors.primary else Color.White
                                    )

                                    val selectedTextColor by animateColorAsState(
                                        targetValue = if (isSelected) Color.White else Color.Black
                                    )

                                    Tab(
                                        selected = isSelected,
                                        onClick = {
                                            exploreViewModel.updateUiConfig {
                                                uiConfig.copy(
                                                    selectedTabCategoryIndex = index,
                                                    selectedCategory = category
                                                )
                                            }
                                            exploreViewModel.getProductCategory(category.id)
                                        },
                                        enabled = isCategoryTabAllowClick,
                                        modifier = Modifier
                                            .padding(
                                                vertical = 12.dp,
                                                horizontal = 6.dp
                                            )
                                            .shadow(4.dp, clip = false, shape = RoundedCornerShape(18.dp))
                                            .clip(RoundedCornerShape(18.dp))
                                            .background(color = selectedTabColor)
                                    ) {

                                        Text(
                                            text = category.name,
                                            modifier = Modifier.padding(8.dp),
                                            fontSize = 12.sp,
                                            color = selectedTextColor
                                        )
                                    }
                                }
                            }
                        }

                        // product in category
                        with(productCategoryState) {
                            onIdle {
                                exploreViewModel.getProductCategory(uiConfig.selectedCategory.id)
                            }
                            onLoading {
                                item {
                                    Shimmer(
                                        modifier = Modifier.width(120.dp)
                                            .height(180.dp)
                                    )
                                }
                            }
                            onSuccess { products ->
                                item {
                                    LazyRow(
                                        contentPadding = PaddingValues(
                                            horizontal = 12.dp
                                        ),
                                        modifier = Modifier.ignoreHorizontalParentPadding(12.dp)
                                    ) {
                                        products.forEach {
                                            item {
                                                ProductItemGridRectangle(it) {

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // brand state
                with(brandState) {
                    onIdle {
                        exploreViewModel.getBrand()
                    }
                    onLoading {
                        item {
                            Shimmer(modifier = Modifier.height(100.dp))
                        }
                    }
                    onSuccess { brands ->
                        item {
                            Text(
                                text = "Brand",
                                modifier = Modifier.padding(6.dp).padding(top = 12.dp),
                                fontWeight = FontWeight.Black
                            )
                        }
                        item {
                            ScrollableTabRow(
                                selectedTabIndex = uiConfig.selectedTabBrandIndex,
                                modifier = Modifier.fillMaxWidth()
                                    .ignoreHorizontalParentPadding(12.dp),
                                backgroundColor = Color.Transparent,
                                edgePadding = 12.dp,
                                indicator = {},
                                divider = {}
                            ) {
                                brands.forEachIndexed { index, brand ->
                                    val isSelected by remember {
                                        derivedStateOf {
                                            index == uiConfig.selectedTabBrandIndex
                                        }
                                    }

                                    val selectedTabColor by animateColorAsState(
                                        targetValue = if (isSelected) MaterialTheme.colors.primary else Color.White
                                    )

                                    val selectedTextColor by animateColorAsState(
                                        targetValue = if (isSelected) Color.White else Color.Black
                                    )

                                    Tab(
                                        selected = isSelected,
                                        onClick = {
                                            exploreViewModel.updateUiConfig {
                                                uiConfig.copy(
                                                    selectedTabBrandIndex = index,
                                                    selectedBrand = brand
                                                )
                                            }
                                            exploreViewModel.getProductBrand(brand.id)
                                        },
                                        enabled = isBrandTabAllowClick,
                                        modifier = Modifier
                                            .padding(
                                                vertical = 12.dp,
                                                horizontal = 6.dp
                                            )
                                            .shadow(4.dp, clip = false, shape = RoundedCornerShape(18.dp))
                                            .clip(RoundedCornerShape(18.dp))
                                            .background(color = selectedTabColor)
                                    ) {

                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val painter = rememberImagePainter(brand.logo)
                                            Image(
                                                painter = painter,
                                                contentDescription = null,
                                                modifier = Modifier.size(14.dp),
                                                colorFilter = ColorFilter.tint(color = selectedTextColor)
                                            )
                                            Text(
                                                text = brand.name,
                                                modifier = Modifier.padding(start = 6.dp),
                                                fontSize = 12.sp,
                                                color = selectedTextColor
                                            )
                                        }

                                    }
                                }
                            }
                        }

                        // product in brand
                        with(productBrandState) {
                            onIdle {
                                exploreViewModel.getProductBrand(uiConfig.selectedBrand.id)
                            }
                            onLoading {
                                item {
                                    Shimmer(
                                        modifier = Modifier.width(120.dp)
                                            .height(180.dp)
                                    )
                                }
                            }
                            onSuccess { products ->
                                item {
                                    LazyRow(
                                        contentPadding = PaddingValues(12.dp),
                                        modifier = Modifier.ignoreHorizontalParentPadding(12.dp)
                                    ) {
                                        products.forEach {
                                            item {
                                                ProductItemGridRectangle(it) {

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            SearchBarStatic(
                modifier = Modifier.fillMaxWidth()
                    .background(color = searchBarColor)
            ) {

            }
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopProductBanner(products: List<ThumbnailProduct>) {
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(5000L)
                val nextPage = (pagerState.currentPage + 1) % products.count()
                pagerState.animateScrollToPage(
                    if (nextPage == products.count() - 1) 0 else nextPage
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
            pageCount = products.count(),
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
            val item = products[it]
            val painter = rememberImagePainter(item.image)
            Box(
                modifier = Modifier.fillMaxHeight()
                    .fillMaxWidth()
                    .shadow(8.dp, clip = false, shape = RoundedCornerShape(Dimens.CornerSize))
                    .clip(RoundedCornerShape(Dimens.CornerSize))
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
                        .align(Alignment.BottomStart)
                        .wrapContentSize()
                        .padding(12.dp)
                ) {

                    Text(
                        text = item.name,
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