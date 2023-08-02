package com.utsman.tokobola.explore.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.seiko.imageloader.rememberImagePainter
import com.utsman.tokobola.common.component.Dimens
import com.utsman.tokobola.common.component.ProductItemGrid
import com.utsman.tokobola.common.component.SearchBarStatic
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.SimpleErrorScreen
import com.utsman.tokobola.common.component.ignoreHorizontalParentPadding
import com.utsman.tokobola.common.component.rememberForeverLazyListState
import com.utsman.tokobola.common.component.tintDark
import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.PlatformUtils
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onIdle
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.explore.LocalExploreUseCase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Explore() {
    val exploreUseCase = LocalExploreUseCase.current

    val exploreViewModel = rememberViewModel { ExploreViewModel(exploreUseCase) }

    val brandState by exploreViewModel.brandState.collectAsState()
    val categoryState by exploreViewModel.categoryState.collectAsState()
    val productCategoryState by exploreViewModel.productCategoryState.collectAsState()

    val categories by exploreViewModel.categories.collectAsState()

    val navigationBarHeight = PlatformUtils.rememberNavigationBarHeight()

    val lazyGridState = rememberForeverLazyListState("explore_grid")

    val isLoading by derivedStateOf {
        categoryState is State.Loading
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = {
            exploreViewModel.restartData()
        }
    )

    val uiConfig by exploreViewModel.uiConfig.collectAsState()

    val offsetTabCategory = animateDpAsState(
        targetValue = if (uiConfig.offsetTabCategory <= 0) (-12).dp else (-(uiConfig.heightTabCategory)).dp
    )

    LaunchedEffect(offsetTabCategory) {
        exploreViewModel.updateUiConfig {
            uiConfig.copy(offsetTabCategory = 120f, heightTabCategory = 120)
        }
    }

    Scaffold(
        // do not add top bar here
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    top = (6 + Dimens.HeightTopBarSearch.value).dp,
                    bottom = (6 + (navigationBarHeight * 2)).dp,
                    start = 6.dp,
                    end = 6.dp
                ),
                state = lazyGridState
            ) {
                with(brandState) {
                    onIdle { exploreViewModel.getBrand() }
                    onLoading {
                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ) {
                            Shimmer(
                                modifier = Modifier.height(120.dp)
                            )
                        }
                    }
                    onSuccess { brands ->
                        items(
                            items = brands,
                            span = { GridItemSpan(this.maxLineSpan) }
                        ) {
                            BrandItem(it)
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

                item(
                    key = "tab",
                    span = { GridItemSpan(this.maxLineSpan) }
                ) {
                    // tab
                    TabCategory(
                        modifier = Modifier,
                        categories = categories,
                        exploreViewModel = exploreViewModel,
                        lazyGridState = lazyGridState,
                        onFirstItemOffset = {
                            //offsetSticky = it
                        },
                        onGlobalOffset = { offset, height ->
                            exploreViewModel.updateUiConfig {
                                uiConfig.copy(offsetTabCategory = offset, heightTabCategory = height)
                            }
                        }
                    )
                }

                with(categoryState) {
                    onIdle { exploreViewModel.getCategory() }
                    onLoading {
                        items(listOf(1, 2)) {
                            Shimmer(modifier = Modifier.height(120.dp))
                        }
                    }
                    onSuccess { categories ->
                        exploreViewModel.pushCategories(categories)
                        exploreViewModel.updateUiConfig {
                            // first category
                            uiConfig.copy(selectedCategory = categories[uiConfig.selectedTabCategory])
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

                with(productCategoryState) {
                    onIdle { exploreViewModel.getProductCategory(1) }
                    onLoading {
                        items(listOf(1, 2)) {
                            Shimmer(modifier = Modifier.height(120.dp))
                        }
                    }
                    onSuccess { products ->
                        items(products) {
                            ProductItemGrid(it) {

                            }
                        }

                        item(
                            span = { GridItemSpan(this.maxLineSpan) }
                        ) {
                            val painterCurrentCategory = rememberImagePainter(uiConfig.selectedCategory.image)

                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(6.dp)
                                    .height(80.dp)
                                    .clickable {  }
                            ) {
                                Image(
                                    painter = painterCurrentCategory,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    colorFilter = ColorFilter.tintDark(),
                                    modifier = Modifier.fillMaxSize()
                                )

                                Text(
                                    text = "More on '${uiConfig.selectedCategory.name}'",
                                    modifier = Modifier.padding(12.dp),
                                    color = Color.White
                                )
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
            }

            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            Box(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentSize()
                    .offset(y = offsetTabCategory.value + 120.dp)
                    .background(color = Color.White)
            ) {
                TabCategory(
                    categories = categories,
                    exploreViewModel = exploreViewModel,
                    lazyGridState = lazyGridState,
                    onFirstItemOffset = {},
                    onGlobalOffset = { _, _ ->}
                )
            }

            SearchBarStatic(
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
            ) {
                // action click search
            }
        }
    }
}

@Composable
fun BrandItem(brand: Brand) {
    Box(
        modifier = Modifier.fillMaxWidth().height(120.dp).padding(6.dp)
    ) {
        val brandImagePainter = rememberImagePainter(brand.image)
        val brandLogoPainter = rememberImagePainter(brand.logo)

        Image(
            painter = brandImagePainter,
            contentDescription = brand.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tintDark()
        )

        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            Text(
                text = brand.name,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            Image(
                painter = brandLogoPainter,
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Box(
        modifier = Modifier.fillMaxWidth().height(120.dp).padding(6.dp)
    ) {
        val categoryPainter = rememberImagePainter(category.image)
        Image(
            painter = categoryPainter,
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(
                color = Color.Black.copy(0.2f), blendMode = BlendMode.Multiply
            )
        )

        Text(
            text = category.name,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun TabCategory(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    exploreViewModel: ExploreViewModel,
    lazyGridState: LazyGridState,
    onFirstItemOffset: (Float) -> Unit,
    onGlobalOffset: (Float, Int) -> Unit
) {
    val uiConfig by exploreViewModel.uiConfig.collectAsState()
    val scope = rememberCoroutineScope()

    if (categories.isNotEmpty()) {
        ScrollableTabRow(
            selectedTabIndex = uiConfig.selectedTabCategory,
            modifier = Modifier.fillMaxWidth()
                .then(modifier)
                .ignoreHorizontalParentPadding(12.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    val size = layoutCoordinates.size
                    val offsetY =
                        layoutCoordinates.localToWindow(Offset(0f, 0f)).y
                    onGlobalOffset.invoke(offsetY, size.height)
                },
            backgroundColor = Color.Transparent,
            edgePadding = 0.dp
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = index == uiConfig.selectedTabCategory,
                    onClick = {
                        /*scope.launch {
                            lazyGridState.scrollToItem(8)
                        }*/
                        exploreViewModel.updateUiConfig {
                            uiConfig.copy(selectedTabCategory = index, selectedCategory = category)
                        }
                        exploreViewModel.getProductCategory(category.id)
                    },
                    modifier = Modifier.wrapContentSize()
                ) {
                    Box(
                        modifier = Modifier.wrapContentSize()
                    ) {
                        val tabPainter = rememberImagePainter(category.image)
                        val screenHeight = LocalDensity.current.density
                        Text(
                            category.name,
                            modifier = Modifier.padding(12.dp).zIndex(2f)
                                .onGloballyPositioned {
                                    if (index == 0) {
                                        val itemY = it.positionInRoot().y
                                        val offsetSticky = itemY.coerceAtLeast(screenHeight - 30)
                                        onFirstItemOffset.invoke(offsetSticky)
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}