package com.utsman.tokobola.details.domain

import com.utsman.tokobola.common.mapToProduct
import com.utsman.tokobola.common.entity.Product
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.StateTransformation

class DetailUseCase(private val repository: DetailRepository) {

    val productDetailReducer = ApiReducer<Product>()

    suspend fun getDetail(id: Int) {
        productDetailReducer.transform(
            call = { repository.getDetailProduct(id) },
            mapper = {
                it.data?.mapToProduct() ?: Product()
            }
        )
    }

    suspend fun markProductViewed(productId: Int) {
        repository.markAsViewed(productId)
    }
    suspend fun clearDetail() {
        productDetailReducer.clear()
    }
}