package com.utsman.tokobola.explore.domain.search

import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.data.orFalse
import com.utsman.tokobola.core.data.orNol
import com.utsman.tokobola.explore.domain.ExploreRepository
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.AutoPagingAdapter
import kotlinx.coroutines.flow.MutableStateFlow


class SearchUseCase(private val repository: ExploreRepository) {

    val productSearchReducer = ApiReducer<Paged<ThumbnailProduct>>()

    private val searchPagingAdapter = AutoPagingAdapter(productSearchReducer)

    val query = MutableStateFlow("")
    private var currentQuery = ""

    suspend fun getBySearch(query: String) {
        if (currentQuery != query) {
            currentQuery = query
            searchPagingAdapter.clear()
        }

        when {
            (query.count() < 3) -> {
                currentQuery = ""
                searchPagingAdapter.clear()
            }
            else -> {
                searchPagingAdapter.executeResponse(
                    call = {
                        repository.getProductBySearch(it, query)
                    },
                    mapper = {
                        it.toThumbnailProduct()
                    }
                )
            }
        }
    }

    fun clearData() {
        productSearchReducer.clear()
        query.value = ""
    }

    companion object : SingletonCreator<SearchUseCase>()
}