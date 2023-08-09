package com.utsman.tokobola.home.domain

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.HomeBanner
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toBrand
import com.utsman.tokobola.common.toHomeBanner
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.core.data.orFalse
import com.utsman.tokobola.core.data.orNol
import com.utsman.tokobola.core.utils.IoScope
import com.utsman.tokobola.core.utils.pmap
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.StateTransformation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

class HomeUseCase(private val homeRepository: HomeRepository) {

    val productsFeaturedReducer = ApiReducer<Paged<ThumbnailProduct>>()
    val productBannerReducer = ApiReducer<List<HomeBanner>>()
    val brandReducer = ApiReducer<List<Brand>>()

    val productViewedReducer = ApiReducer<List<ThumbnailProduct>>()

    private var currentPageProduct: Int = 1
    private var prevPageProduct: Int = 1
    private var hasNextPageProduct = true

    private val prevListProduct: MutableList<ThumbnailProduct> = mutableListOf()

    suspend fun getProduct() {
        if (hasNextPageProduct) {
            productsFeaturedReducer.transform(
                call = {
                    homeRepository.getFeaturedProductPaged(currentPageProduct).apply {
                        hasNextPageProduct = this.data?.hasNextPage.orFalse()
                        prevPageProduct = currentPageProduct
                    }
                },
                mapper = { pagedResponseProduct ->
                    val dataPaged = pagedResponseProduct.data
                    val dataProduct = dataPaged?.data.orEmpty()
                        .map {
                            it.toThumbnailProduct()
                        }

                    currentPageProduct = dataPaged?.page.orNol() + 1

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

    suspend fun getAllProductViewed() {
        homeRepository.getAllRecentlyViewedFlow()
            .collect { realms ->
                productViewedReducer.transform(
                    transformation = StateTransformation.SimpleResponseTransform(),
                    call = {
                        val ids = realms.map { it.productId }
                        homeRepository.getThumbnailByIds(ids)
                    },
                    mapper = { thumbnailProductResponse ->
                        thumbnailProductResponse.data
                            ?.map { it.toThumbnailProduct() }
                            .orEmpty()
                    }
                )
            }
    }

    fun clearProductPage() {
        hasNextPageProduct = true
        currentPageProduct = 1
        prevListProduct.clear()
        brandReducer.clear()
        productViewedReducer.clear()
    }
}