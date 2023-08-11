package com.utsman.tokobola.wishlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.utsman.tokobola.common.component.ErrorScreen
import com.utsman.tokobola.common.component.ProductItemGrid
import com.utsman.tokobola.common.component.ScaffoldGridState
import com.utsman.tokobola.common.component.SearchBarStatic
import com.utsman.tokobola.common.component.Shimmer
import com.utsman.tokobola.common.component.animatedTopBarColor
import com.utsman.tokobola.core.rememberViewModel
import com.utsman.tokobola.core.utils.onFailure
import com.utsman.tokobola.core.utils.onIdle
import com.utsman.tokobola.core.utils.onLoading
import com.utsman.tokobola.core.utils.onSuccess
import com.utsman.tokobola.wishlist.LocalWishlistUseCase

@Composable
fun Wishlist() {
    val useCase = LocalWishlistUseCase.current
    val viewModel = rememberViewModel { WishlistViewModel(useCase) }
    
    val productState by viewModel.productWishlistState.collectAsState()

    val lazyGridState = rememberLazyGridState()

    val searchBarColor by lazyGridState.animatedTopBarColor
    
    ScaffoldGridState(
        topBar = {
            SearchBarStatic(
                modifier = Modifier
                    .background(color = searchBarColor)
            )
        },
        lazyGridState = lazyGridState,
        modifier = Modifier.fillMaxSize()
    ) {
        with(productState) {
            onIdle { 
                viewModel.listenProductWishlist()
            }
            onLoading {
                items(
                    items = listOf(1, 2)
                ) {
                    Shimmer()
                }
            }
            onSuccess { products -> 
                items(products) { product ->
                    ProductItemGrid(product)
                }
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