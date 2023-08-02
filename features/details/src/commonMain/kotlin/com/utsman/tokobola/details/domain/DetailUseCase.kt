package com.utsman.tokobola.details.domain

import com.utsman.tokobola.common.mapToProduct
import com.utsman.tokobola.common.entity.Product
import com.utsman.tokobola.network.ApiReducer

class DetailUseCase(private val repository: DetailRepository) {

    private val productDetailReducer = ApiReducer<Product>()
    val productDetailFlow get() = productDetailReducer.dataFlow

    suspend fun getDetail(id: Int) {
        productDetailReducer.transform(
            call = { repository.getDetailProduct(id) },
            mapper = {
                it.data?.mapToProduct() ?: Product()
            }
        )
    }

    suspend fun clearDetail() {
        productDetailReducer.clear()
    }
}