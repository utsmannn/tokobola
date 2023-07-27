package com.utsman.tokobola.home.domain

import com.utsman.tokobola.common.entity.ui.HomeBanner
import com.utsman.tokobola.common.entity.ui.ThumbnailProduct
import com.utsman.tokobola.common.toHomeBanner
import com.utsman.tokobola.common.toHomeProduct
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.data.orFalse
import com.utsman.tokobola.network.ApiReducer

class HomeUseCase(private val homeRepository: HomeRepository) {

    val productListReducer = ApiReducer<Paged<ThumbnailProduct>>()
    val productBanner = ApiReducer<List<HomeBanner>>()

    suspend fun getProduct() {
        productListReducer.transform(
            call = {
                homeRepository.getProductPaged()
            },
            mapper = { pagedResponseProduct ->
                val dataPaged = pagedResponseProduct.data
                val dataProduct = dataPaged?.data.orEmpty()
                    .map {
                        it.toHomeProduct()
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

    suspend fun getBanner() {
        productBanner.transform(
            call = {
                homeRepository.getBanner()
            },
            mapper = { bannerResponse ->
                bannerResponse.data?.map { it.toHomeBanner() }.orEmpty()
            }
        )
    }
}