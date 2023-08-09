package com.utsman.tokobola.explore.ui.explore

import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.explore.domain.explore.ExploreUseCase
import com.utsman.tokobola.explore.ui.ExploreUiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(private val useCase: ExploreUseCase) : ViewModel() {

    val brandState get() = useCase.brandReducer.dataFlow
    val categoryState get() = useCase.categoryReducer.dataFlow

    val productBrandState get() = useCase.productBrandReducer.dataFlow
    val productCategoryState get() = useCase.productCategoryReducer.dataFlow

    val topProductState get() = useCase.topProductReducer.dataFlow
    val curatedProductState get() = useCase.curatedProductReducer.dataFlow

    val uiConfig: MutableStateFlow<ExploreUiConfig> = MutableStateFlow(ExploreUiConfig())

    fun getBrand() = viewModelScope.launch {
        useCase.getBrand()
    }

    fun getCategory() = viewModelScope.launch {
        useCase.getCategory()
    }

    fun getProductBrand(brandId: Int) = viewModelScope.launch {
        useCase.getProductBrand(brandId)
    }
    fun getProductCategory(categoryId: Int) = viewModelScope.launch {
        useCase.getProductCategory(categoryId)
    }

    fun getTopProduct() = viewModelScope.launch {
        useCase.getTopProduct()
    }

    fun getCuratedProduct() = viewModelScope.launch {
        useCase.getCuratedProduct()
    }

    fun updateUiConfig(uiConfig: () -> ExploreUiConfig) {
        val newUiConfig = uiConfig.invoke()
        this.uiConfig.value = newUiConfig
    }

    fun restartData() {
        useCase.clearData()
        getBrand()
        getCategory()
    }
}