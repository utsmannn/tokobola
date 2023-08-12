package com.utsman.tokobola.details.domain

import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.AutoPagingAdapter

class BrandDetailUseCase(private val detailRepository: DetailRepository) {

    val productPagedReducer = ApiReducer<Paged<ThumbnailProduct>>()
    private val productPagedAdapter = AutoPagingAdapter(productPagedReducer)

    suspend fun getProduct(brandId: Int) {
        productPagedAdapter.executeResponse(
            call = { page ->
                detailRepository.getProductBrand(brandId, page)
            },
            mapper = {
                it.toThumbnailProduct()
            }
        )
    }

    fun clear() = productPagedAdapter.clear()

    companion object : SingletonCreator<BrandDetailUseCase>()
}