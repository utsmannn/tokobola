package com.utsman.tokobola.details.ui

import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.details.domain.DetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val detailUseCase: DetailUseCase) : ViewModel() {

    val detailState get() = detailUseCase.productDetailReducer.dataFlow.asStateFlow()

    val uiConfig = MutableStateFlow(DetailUiConfig())

    val productCart get() = detailUseCase.productCart.asStateFlow()

    fun getDetail(productId: Int) = viewModelScope.launch {
        detailUseCase.getDetail(productId)
    }

    fun postProductViewed(productId: Int) = viewModelScope.launch {
        detailUseCase.markProductViewed(productId)
    }

    fun updateUiConfig(uiConfig: () -> DetailUiConfig) {
        val newUiConfig = uiConfig.invoke()
        this.uiConfig.value = newUiConfig
    }

    fun getCart(productId: Int) = viewModelScope.launch {
        detailUseCase.getProductCart(productId)
    }

    fun incrementCart(productId: Int) = viewModelScope.launch {
        detailUseCase.incrementCart(productId)
    }

    fun decrementCart(productId: Int) = viewModelScope.launch {
        detailUseCase.decrementCart(productId)
    }

    override fun onCleared() {
        viewModelScope.launch {
            detailUseCase.clearDetail()
        }
        super.onCleared()
    }
}