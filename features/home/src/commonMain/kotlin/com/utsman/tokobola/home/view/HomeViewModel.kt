package com.utsman.tokobola.home.view

import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.home.domain.HomeUseCase
import kotlinx.coroutines.launch

class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {

    val homeProduct get() = homeUseCase.productListReducer.dataFlow

    fun getHomeProduct() = viewModelScope.launch {
        homeUseCase.getProduct()
    }
}