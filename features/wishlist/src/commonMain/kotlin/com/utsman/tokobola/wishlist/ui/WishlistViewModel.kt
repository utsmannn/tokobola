package com.utsman.tokobola.wishlist.ui

import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.wishlist.domain.WishlistUseCase
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WishlistViewModel(private val useCase: WishlistUseCase) : ViewModel() {

    val productWishlistState get() = useCase.productWishlistReducer.dataFlow.asStateFlow()

    fun listenProductWishlist() = viewModelScope.launch {
        useCase.getWishlist()
    }
}