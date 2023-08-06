package com.utsman.tokobola.home.domain

import com.utsman.tokobola.api.productWebApi
import com.utsman.tokobola.database.data.ThumbnailProductRealm
import com.utsman.tokobola.database.localRepository
import kotlinx.coroutines.flow.Flow

class HomeRepository {

    private val productApi by productWebApi()
    private val localRepository by localRepository()

    suspend fun getFeaturedProductPaged(page: Int) = productApi.getByFeaturedPaged(page)
    suspend fun getBanner() = productApi.getHomeBanner()
    suspend fun getBrand() = productApi.getBrand()

    suspend fun markAsViewed(thumbnailProductRealm: ThumbnailProductRealm) {
        localRepository.insertThumbnailProduct(thumbnailProductRealm)
    }

    suspend fun getAllViewed(): List<ThumbnailProductRealm> {
        return localRepository.selectAllThumbnailProduct()
    }

    suspend fun getAllViewedFlow(): Flow<List<ThumbnailProductRealm>> {
        return localRepository.selectAllThumbnailProductFlow()
    }
}