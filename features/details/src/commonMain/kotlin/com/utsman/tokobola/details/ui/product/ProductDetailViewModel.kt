package com.utsman.tokobola.details.ui.product

import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.details.domain.ProductDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val productDetailUseCase: ProductDetailUseCase) : ViewModel() {

    val detailState get() = productDetailUseCase.productDetailReducer.dataFlow.asStateFlow()
    val wishlistState get() = productDetailUseCase.isProductExist.asStateFlow()

    val uiConfig = MutableStateFlow(ProductDetailUiConfig())

    val productCart get() = productDetailUseCase.productCart.asStateFlow()

    fun getDetail(productId: Int) = viewModelScope.launch {
        productDetailUseCase.getDetail(productId)
    }

    fun postProductViewed(productId: Int) = viewModelScope.launch {
        productDetailUseCase.markProductViewed(productId)
    }

    fun updateUiConfig(uiConfig: () -> ProductDetailUiConfig) {
        val newUiConfig = uiConfig.invoke()
        this.uiConfig.value = newUiConfig
    }

    fun getCart(productId: Int) = viewModelScope.launch {
        productDetailUseCase.getProductCart(productId)
    }

    fun incrementCart(productId: Int) = viewModelScope.launch {
        productDetailUseCase.incrementCart(productId)
    }

    fun decrementCart(productId: Int) = viewModelScope.launch {
        productDetailUseCase.decrementCart(productId)
    }

    fun listenWishlist(productId: Int) = viewModelScope.launch {
        productDetailUseCase.listenIsWishlistExist(productId)
    }

    fun toggleWishlist(productId: Int) = viewModelScope.launch {
        productDetailUseCase.toggleWishlist(productId)
    }

    override fun onCleared() {
        viewModelScope.launch {
            productDetailUseCase.clearDetail()
        }
        super.onCleared()
    }
}