package com.utsman.tokobola.home.ui

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.home.domain.HomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {

    val productsFeaturedState get() = homeUseCase.productsFeaturedReducer.dataFlow
    val homeBannerState get() = homeUseCase.productBannerReducer.dataFlow
    val brandState get() = homeUseCase.brandReducer.dataFlow

    val productViewed get() = homeUseCase.productViewedReducer.dataFlow

    val productsFeaturedFlow = MutableStateFlow(emptyList<ThumbnailProduct>())
    val brandListFlow = MutableStateFlow(emptyList<Brand>())

    val isRestart = MutableStateFlow(false)

    fun getHomeProduct() = viewModelScope.launch {
        isRestart.value = true
        homeUseCase.getProduct()
    }

    fun getHomeBanner() = viewModelScope.launch {
        homeUseCase.getBanner()
    }

    fun getProductViewed() = viewModelScope.launch {
        homeUseCase.getAllProductViewed()
    }

    fun getBrand() = viewModelScope.launch {
        homeUseCase.getBrand()
    }

    fun restartData() {
        isRestart.value = true
        productsFeaturedFlow.value = emptyList()
        brandListFlow.value = emptyList()
        homeUseCase.clearProductPage()
        getHomeBanner()
        getHomeProduct()
        getBrand()
        getProductViewed()
    }

    fun postProduct(list: List<ThumbnailProduct>) {
        isRestart.value = false
        productsFeaturedFlow.value = list
    }

    fun postBrandList(list: List<Brand>) {
        brandListFlow.value = list
    }
}