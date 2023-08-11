package com.utsman.tokobola.home.domain

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.HomeBanner
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toBrand
import com.utsman.tokobola.common.toHomeBanner
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.core.data.Paged
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.AutoPagingAdapter
import com.utsman.tokobola.network.StateTransformation

class HomeUseCase(private val homeRepository: HomeRepository) {

    val productsFeaturedReducer = ApiReducer<Paged<ThumbnailProduct>>()
    val productBannerReducer = ApiReducer<List<HomeBanner>>()
    val brandReducer = ApiReducer<List<Brand>>()

    val productViewedReducer = ApiReducer<List<ThumbnailProduct>>()

    private val featurePagingAdapter = AutoPagingAdapter(productsFeaturedReducer)

    suspend fun getProduct() {
        featurePagingAdapter.executeResponse(
            call = {
                homeRepository.getFeaturedProductPaged(it)
            },
            mapper = {
                it.toThumbnailProduct()
            }
        )
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
                    transformation = StateTransformation.SimpleTransform(),
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
        featurePagingAdapter.clear()
        brandReducer.clear()
        productViewedReducer.clear()
    }
}