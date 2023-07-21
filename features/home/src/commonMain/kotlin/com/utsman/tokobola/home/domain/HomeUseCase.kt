package com.utsman.tokobola.home.domain

import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.orFalse
import com.utsman.tokobola.home.entity.Product
import com.utsman.tokobola.home.entity.mapToProduct
import com.utsman.tokobola.network.ApiReducer

class HomeUseCase(private val homeRepository: HomeRepository) {

    val productListReducer = ApiReducer<Paged<Product>>()

    suspend fun getProduct() {
        productListReducer.transform(
            call = {
                homeRepository.getProductPaged()
            },
            mapper = { pagedResponseProduct ->
                val dataPaged = pagedResponseProduct.data
                val dataProduct = dataPaged?.data.orEmpty()
                    .map {
                        it.mapToProduct()
                    }
                Paged(
                    data = dataProduct,
                    hasNextPage = dataPaged?.hasNextPage.orFalse(),
                    page = dataPaged?.page ?: 1,
                    perPage = dataPaged?.perPage ?: 10
                )
            }
        )
    }
}