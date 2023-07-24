package com.utsman.tokobola.details.domain

import com.utsman.tokobola.details.entity.ProductDetail
import com.utsman.tokobola.details.entity.mapToProduct
import com.utsman.tokobola.network.ApiReducer

class DetailUseCase(private val repository: DetailRepository) {

    private val productDetailReducer = ApiReducer<ProductDetail>()
    val productDetailFlow get() = productDetailReducer.dataFlow

    suspend fun getDetail(id: Int) {
        productDetailReducer.transform(
            call = { repository.getDetailProduct(id) },
            mapper = {
                it.data?.mapToProduct() ?: ProductDetail()
            }
        )
    }

    suspend fun clearDetail() {
        productDetailReducer.clear()
    }
}