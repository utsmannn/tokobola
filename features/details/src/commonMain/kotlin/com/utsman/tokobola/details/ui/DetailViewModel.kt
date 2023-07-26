package com.utsman.tokobola.details.ui

import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.details.domain.DetailUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val detailUseCase: DetailUseCase) : ViewModel() {

    val detailState get() = detailUseCase.productDetailFlow

    fun getDetail(id: Int) = viewModelScope.launch {
        detailUseCase.getDetail(id)
    }

    override fun onCleared() {
        println("data clear....")
        viewModelScope.launch {
            detailUseCase.clearDetail()
        }
        super.onCleared()
    }
}