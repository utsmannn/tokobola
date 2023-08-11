package com.utsman.tokobola.details.ui.brand

import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.details.domain.BrandDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BrandDetailViewModel(private val useCase: BrandDetailUseCase) : ViewModel() {

    val productListState get() = useCase.productPagedReducer.dataFlow
    val productListFlow = MutableStateFlow(emptyList<ThumbnailProduct>())
    val brandTitle = MutableStateFlow("")

    fun getProduct(brandId: Int) = viewModelScope.launch {
        useCase.getProduct(brandId)
    }

    fun pushProductList(productList: List<ThumbnailProduct>) = viewModelScope.launch {
        productListFlow.value = productList
        brandTitle.value = productList.firstOrNull()?.brand?.name.orEmpty()
    }

    fun clearData() {
        useCase.clear()
    }
}