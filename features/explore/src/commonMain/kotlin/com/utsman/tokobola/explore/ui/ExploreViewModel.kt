package com.utsman.tokobola.explore.ui

import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.explore.domain.ExploreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(private val useCase: ExploreUseCase) : ViewModel() {

    val brandState get() = useCase.brandReducer.dataFlow
    val categoryState get() = useCase.categoryReducer.dataFlow

    private var _offsetTabCategory = MutableStateFlow(-10f)
    val offsetTabCategory: StateFlow<Float> get() = _offsetTabCategory

    private var _heightTabCategory = MutableStateFlow(0)
    val heightTabCategory: StateFlow<Int> get() = _heightTabCategory

    val categories: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())

    fun getBrand() = viewModelScope.launch {
        useCase.getBrand()
    }

    fun getCategory() = viewModelScope.launch {
        useCase.getCategory()
    }

    fun pushCategories(categories: List<Category>) {
        this.categories.value = categories
    }

    fun pushOffsetCategory(offset: Float, height: Int) {
        _offsetTabCategory.value = offset
        _heightTabCategory.value = height
    }

    fun restartData() {
        useCase.clearData()
        getBrand()
        getCategory()
    }
}