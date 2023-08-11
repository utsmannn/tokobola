package com.utsman.tokobola.explore.ui.search

import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.explore.domain.search.SearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(private val useCase: SearchUseCase) : ViewModel() {

    val query get() = useCase.query

    val productSearchState get() = useCase.productSearchReducer.dataFlow
    val productSearchFlow = MutableStateFlow(emptyList<ThumbnailProduct>())

    fun postResultSearch(list: List<ThumbnailProduct>) {
        productSearchFlow.value = list
    }

    fun listenQuery() = viewModelScope.launch {
        query.debounce(2000)
            .distinctUntilChanged()
            .collectLatest {
                useCase.getBySearch(it)
            }
    }

    fun getNextSearch() = viewModelScope.launch {
        useCase.getBySearch(query.value)
    }

    fun clearData() {
        useCase.clearData()
    }
}