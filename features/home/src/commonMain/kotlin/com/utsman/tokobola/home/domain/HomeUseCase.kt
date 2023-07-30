package com.utsman.tokobola.home.domain

import com.utsman.tokobola.common.entity.ui.Brand
import com.utsman.tokobola.common.entity.ui.HomeBanner
import com.utsman.tokobola.common.entity.ui.ThumbnailProduct
import com.utsman.tokobola.common.toBrand
import com.utsman.tokobola.common.toHomeBanner
import com.utsman.tokobola.common.toHomeProduct
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.data.orFalse
import com.utsman.tokobola.core.data.orNol
import com.utsman.tokobola.network.ApiReducer

class HomeUseCase(private val homeRepository: HomeRepository) {

    val productListReducer = ApiReducer<Paged<ThumbnailProduct>>()
    val productBannerReducer = ApiReducer<List<HomeBanner>>()
    val brandReducer = ApiReducer<List<Brand>>()

    private var currentPageProduct: Int = 1
    private var prevPageProduct: Int = 1
    private var hasNextPageProduct = true

    private val prevListProduct: MutableList<ThumbnailProduct> = mutableListOf()

    suspend fun getProduct() {
        if (hasNextPageProduct) {
            productListReducer.transform(
                call = {
                    homeRepository.getProductPaged(currentPageProduct).apply {
                        hasNextPageProduct = this.data?.hasNextPage.orFalse()
                        prevPageProduct = currentPageProduct
                    }
                },
                mapper = { pagedResponseProduct ->
                    val dataPaged = pagedResponseProduct.data
                    val dataProduct = dataPaged?.data.orEmpty()
                        .map {
                            it.toHomeProduct()
                        }

                    currentPageProduct = dataPaged?.page.orNol()+1

                    prevListProduct.addAll(dataProduct)
                    Paged(
                        data = prevListProduct,
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
        productBannerReducer.transform(
            call = {
                homeRepository.getBanner()
            },
            mapper = { bannerResponse ->
                bannerResponse.data?.map { it.toHomeBanner() }.orEmpty()
            }
        )
    }

    suspend fun getBrand() {
        brandReducer.transform(
            call = {
                homeRepository.getBrand()
            },
            mapper = { brandResponse ->
                brandResponse.data?.map { it.toBrand() }.orEmpty()
                    .filter {
                        // filter "other" brand
                        it.id != 40
                    }
            }
        )
    }

    fun restartProductPage() {
        hasNextPageProduct = true
        currentPageProduct = 1
        prevListProduct.clear()
        brandReducer.clear()
    }
}