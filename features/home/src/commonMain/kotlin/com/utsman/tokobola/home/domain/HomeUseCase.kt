package com.utsman.tokobola.home.domain

import com.utsman.tokobola.common.entity.ui.HomeBanner
import com.utsman.tokobola.common.entity.ui.ThumbnailProduct
import com.utsman.tokobola.common.toHomeBanner
import com.utsman.tokobola.common.toHomeProduct
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.data.orFalse
import com.utsman.tokobola.core.data.orNol
import com.utsman.tokobola.network.ApiReducer

class HomeUseCase(private val homeRepository: HomeRepository) {

    val productListReducer = ApiReducer<Paged<ThumbnailProduct>>()
    val productBanner = ApiReducer<List<HomeBanner>>()

    private var currentPage: Int = 1
    private var hasNextPage = true

    private var prevList: List<ThumbnailProduct> = mutableListOf()

    suspend fun getProduct() {
        if (hasNextPage) {
            productListReducer.transform(
                call = {
                    homeRepository.getProductPaged(currentPage).apply {
                        hasNextPage = this.data?.hasNextPage.orFalse()
                    }
                },
                mapper = { pagedResponseProduct ->
                    val dataPaged = pagedResponseProduct.data
                    val dataProduct = dataPaged?.data.orEmpty()
                        .map {
                            it.toHomeProduct()
                        }

                    val newList = if (currentPage != dataPaged?.page.orNol()) {
                        currentPage = dataPaged?.page.orNol()
                        prevList+dataProduct
                    } else {
                        dataProduct
                    }
                    currentPage++
                    prevList = newList
                    Paged(
                        data = newList,
                        hasNextPage = dataPaged?.hasNextPage.orFalse(),
                        page = dataPaged?.page ?: 1,
                        perPage = dataPaged?.perPage ?: 10
                    )
                }
            )
        } else {
            println("End of reach product!")
        }
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

    fun restartProductPage() {
        currentPage = 1
    }
}