package com.utsman.tokobola.details.view

import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.details.domain.DetailUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val detailUseCase: DetailUseCase) : ViewModel() {

    val detailState get() = detailUseCase.productDetailFlow

    fun getDetail(id: String) = viewModelScope.launch {
        detailUseCase.getDetail(id)
    }
}