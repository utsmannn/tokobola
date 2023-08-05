package com.utsman.tokobola.explore.ui

import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.explore.domain.ExploreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(private val useCase: ExploreUseCase) : ViewModel() {

    val brandState get() = useCase.brandReducer.dataFlow
    val categoryState get() = useCase.categoryReducer.dataFlow

    val productBrandState get() = useCase.productBrandReducer.dataFlow
    val productCategoryState get() = useCase.productCategoryReducer.dataFlow

    val brandAndProductState get() = useCase.brandAndProductReducer.dataFlow
    val categoriesAndProductState get() = useCase.categoryAndProductReducer.dataFlow


    val topProductState get() = useCase.topProductReducer.dataFlow
    val curatedProductState get() = useCase.curatedProductReducer.dataFlow

    val uiConfig: MutableStateFlow<ExploreUiConfig> = MutableStateFlow(ExploreUiConfig())

    val categories: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())

    fun getBrand() = viewModelScope.launch {
        useCase.getBrand()
    }

    fun getCategory() = viewModelScope.launch {
        useCase.getCategory()
    }

    fun getBrandAndProduct() = viewModelScope.launch {
        useCase.getFirstBrandAndProduct()
    }

    fun getCategoriesAndProduct() = viewModelScope.launch {
        useCase.getFirstCategoryAndProduct()
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

    fun pushCategories(categories: List<Category>) {
        this.categories.value = categories
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