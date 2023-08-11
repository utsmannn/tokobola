package com.utsman.tokobola.details.domain

import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.AutoPagingAdapter

class CategoryDetailUseCase(private val detailRepository: DetailRepository) {

    val productPagedReducer = ApiReducer<Paged<ThumbnailProduct>>()
    private val productPagedAdapter = AutoPagingAdapter(productPagedReducer)

    suspend fun getProduct(categoryId: Int) {
        productPagedAdapter.executeResponse(
            call = { page ->
                detailRepository.getProductCategory(categoryId, page)
            },
            mapper = {
                it.toThumbnailProduct()
            }
        )
    }

    fun clear() = productPagedAdapter.clear()
}