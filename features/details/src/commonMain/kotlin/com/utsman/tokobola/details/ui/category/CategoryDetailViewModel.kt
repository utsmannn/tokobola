package com.utsman.tokobola.details.ui.category

import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.details.domain.CategoryDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CategoryDetailViewModel(private val useCase: CategoryDetailUseCase) : ViewModel() {

    val productListState get() = useCase.productPagedReducer.dataFlow
    val productListFlow = MutableStateFlow(emptyList<ThumbnailProduct>())
    val categoryTitle = MutableStateFlow("")

    fun getProduct(categoryId: Int) = viewModelScope.launch {
        useCase.getProduct(categoryId)
    }

    fun pushProductList(productList: List<ThumbnailProduct>) = viewModelScope.launch {
        productListFlow.value = productList
        categoryTitle.value = productList.firstOrNull()?.category?.name.orEmpty()
    }

    override fun onCleared() {
        useCase.clear()
        super.onCleared()
    }
}