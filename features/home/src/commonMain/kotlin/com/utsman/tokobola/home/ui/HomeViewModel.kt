package com.utsman.tokobola.home.ui

import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.home.domain.HomeUseCase
import kotlinx.coroutines.launch

class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {

    val homeProduct get() = homeUseCase.productListReducer.dataFlow
    val homeBanner get() = homeUseCase.productBanner.dataFlow

    fun getHomeProduct() = viewModelScope.launch {
        homeUseCase.getProduct()
    }

    fun getHomeBanner() = viewModelScope.launch {
        homeUseCase.getBanner()
    }
}