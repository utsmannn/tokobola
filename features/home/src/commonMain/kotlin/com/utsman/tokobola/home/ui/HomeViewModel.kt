package com.utsman.tokobola.home.ui

import com.utsman.tokobola.common.entity.ui.ThumbnailProduct
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.home.domain.HomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {

    val homeProduct get() = homeUseCase.productListReducer.dataFlow
    val homeBanner get() = homeUseCase.productBanner.dataFlow

    var productItemCount = 10

    val homeListFlow: MutableStateFlow<List<ThumbnailProduct>> = MutableStateFlow(emptyList())
    val isRestart: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun getHomeProduct() = viewModelScope.launch {
        homeUseCase.getProduct()
    }

    fun getHomeBanner() = viewModelScope.launch {
        homeUseCase.getBanner()
    }

    fun restartData() {
        homeUseCase.restartProductPage()
        viewModelScope.launch { homeUseCase.getBanner() }
        viewModelScope.launch { homeUseCase.getProduct() }
    }

    fun postPaged(list: List<ThumbnailProduct>) {
        homeListFlow.value = list
        productItemCount = list.size
    }
}