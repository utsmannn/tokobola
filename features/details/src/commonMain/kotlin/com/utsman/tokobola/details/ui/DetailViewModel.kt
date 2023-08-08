package com.utsman.tokobola.details.ui

import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.details.domain.DetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val detailUseCase: DetailUseCase) : ViewModel() {

    val detailState get() = detailUseCase.productDetailReducer.dataFlow

    val uiConfig = MutableStateFlow(DetailUiConfig())

    fun getDetail(id: Int) = viewModelScope.launch {
        detailUseCase.getDetail(id)
    }

    fun postProductViewed(productId: Int) = viewModelScope.launch {
        detailUseCase.markProductViewed(productId)
    }

    fun updateUiConfig(uiConfig: () -> DetailUiConfig) {
        val newUiConfig = uiConfig.invoke()
        this.uiConfig.value = newUiConfig
    }

    override fun onCleared() {
        viewModelScope.launch {
            detailUseCase.clearDetail()
        }
        super.onCleared()
    }
}