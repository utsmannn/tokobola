package com.utsman.tokobola.details.ui.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ProductItemGrid
import com.utsman.tokobola.common.component.ScaffoldGridState
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.animatedColor
import com.utsman.tokobola.common.component.animatedTopBarColor
import com.utsman.tokobola.common.component.isScrolledToEnd
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.core.utils.rememberStatusBarHeightDp
import com.utsman.tokobola.details.LocalCategoryDetailUseCase
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryDetail(categoryId: Int) {
    val useCase = LocalCategoryDetailUseCase.current
    val viewModel = rememberViewModel { CategoryDetailViewModel(useCase) }

    val lazyGridState = rememberLazyGridState()

    val isReachBottom by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }

    val productListState by viewModel.productListState.collectAsState()
    val productList by viewModel.productListFlow.collectAsState()
    val categoryTitle by viewModel.categoryTitle.collectAsState()

    var isRetainData by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isRetainData = false
        viewModel.getProduct(categoryId)
    }

    LaunchedEffect(isReachBottom) {
        if (productListState is State.Success && isReachBottom) {
            viewModel.getProduct(categoryId)
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
                text = categoryTitle,
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

@Composable
fun TopBar(text: String, modifier: Modifier = Modifier, lazyGridState: LazyGridState) {
    val navigation = LocalNavigation.current
    val titleColor by lazyGridState.animatedColor(
        from = Color.White,
        to = MaterialTheme.colors.primary
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(34.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(false),
                    onClick = {
                        navigation.back()
                    })
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colors.secondary.copy(alpha = 0.6f)
                )
                .padding(6.dp),
            painter = painterResource(SharedRes.images.arrow_back_default),
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.White)
        )

        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f).padding(12.dp),
            color = titleColor
        )
    }
}