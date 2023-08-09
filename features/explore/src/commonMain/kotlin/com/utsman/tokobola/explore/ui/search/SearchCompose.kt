package com.utsman.tokobola.explore.ui.search

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utsman.tokobola.common.component.Dimens
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ProductItemGrid
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.isScrolledToEnd
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.navigation.LocalNavigation
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.PlatformUtils
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.explore.LocalSearchUseCase
import com.utsman.tokobola.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun Search() {
    val searchUseCase = LocalSearchUseCase.current
    val viewModel = rememberViewModel { SearchViewModel(searchUseCase) }

    val navigationBarHeight = PlatformUtils.rememberNavigationBarHeightDp()

    val lazyGridState = rememberLazyGridState()

    val isColorizeSearchBar by remember {
        derivedStateOf { !lazyGridState.canScrollBackward }
    }

    val searchBarColor = animateColorAsState(
        targetValue = if (isColorizeSearchBar) Color.Transparent else MaterialTheme.colors.primary
    )

    val isReachBottom by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }

    val focusManager = LocalFocusManager.current

    val productSearchState by viewModel.productSearchState.collectAsState()
    val productSearch by viewModel.productSearchFlow.collectAsState()

    var isRetainData by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isRetainData = false
        viewModel.listenQuery()
        focusManager.moveFocus(FocusDirection.Next)
    }

    LaunchedEffect(isReachBottom) {
        if (productSearchState is State.Success && isReachBottom) {
            viewModel.getNextSearch()
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            if (!isRetainData) {
                viewModel.clearData()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    top = (Dimens.HeightTopBarSearch.value).dp,
                    bottom = (6 + (navigationBarHeight.value * 2)).dp,
                    start = 6.dp,
                    end = 6.dp
                ),
                state = lazyGridState
            ) {

                items(
                    items = productSearch
                ) {
                    ProductItemGrid(it) {
                        isRetainData = true
                    }
                }

                with(productSearchState) {
                    onLoading {
                        items(
                            items = listOf(1, 2)
                        ) {
                            Shimmer()
                        }
                    }
                    onSuccess { paged ->
                        focusManager.clearFocus(true)
                        viewModel.postResultSearch(paged.data)
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

            SearchBar(
                modifier = Modifier
                    .background(color = searchBarColor.value),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, viewModel: SearchViewModel) {
    val statusBarHeight = PlatformUtils.rememberStatusBarHeightDp()

    val query by viewModel.query.collectAsState()
    val navigation = LocalNavigation.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.HeightTopBarSearch)
            .padding(
                top = 12.dp + statusBarHeight,
                bottom = 6.dp,
                start = 12.dp,
                end = 12.dp
            )
            .shadow(
                elevation = 128.dp,
                shape = RoundedCornerShape(18.dp)
            ),
        verticalAlignment = Alignment.CenterVertically

    ) {
        val backResources = SharedRes.images.arrow_back_default
        val painter = painterResource(backResources)
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
                .padding(6.dp)
            ,
            painter = painter,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.secondaryVariant)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(18.dp))
                .background(color = Color.White)
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                )
        ) {

            BasicTextField(
                value = query,
                onValueChange = {
                    viewModel.query.value = it
                },
                textStyle = TextStyle.Default.copy(fontSize = 14.sp, color = MaterialTheme.colors.primary),
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                ),
                decorationBox = { innerField ->
                    if (query.isEmpty()) {
                        Text("Search for a product", modifier = Modifier.alpha(0.4f), fontSize = 14.sp)
                    }
                    innerField.invoke()
                }
            )
        }
    }
}