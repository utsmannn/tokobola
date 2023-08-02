package com.utsman.tokobola.explore.ui

import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.explore.domain.ExploreUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExploreViewModel(private val useCase: ExploreUseCase) : ViewModel() {

    val brandState get() = useCase.brandReducer.dataFlow
    val categoryState get() = useCase.categoryReducer.dataFlow
    val productCategoryState get() = useCase.productCategory.dataFlow

    val uiConfig: MutableStateFlow<ExploreUiConfig> = MutableStateFlow(ExploreUiConfig())

    val categories: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())

    fun getBrand() = viewModelScope.launch {
        useCase.getBrand()
    }

    fun getCategory() = viewModelScope.launch {
        useCase.getCategory()
    }

    fun getProductCategory(categoryId: Int) = viewModelScope.launch {
        useCase.getProductCategory(categoryId)
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