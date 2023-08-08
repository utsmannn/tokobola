package com.utsman.tokobola.home.domain

import com.utsman.tokobola.api.productWebApi
import com.utsman.tokobola.database.data.RecentlyViewedRealm
import com.utsman.tokobola.database.data.ThumbnailProductRealm
import com.utsman.tokobola.database.localRepository
import io.realm.kotlin.ext.copyFromRealm
import kotlinx.coroutines.flow.Flow

class HomeRepository {

    private val productApi by productWebApi()
    private val localRepository by localRepository()

    suspend fun getFeaturedProductPaged(page: Int) = productApi.getByFeaturedPaged(page)
    suspend fun getBanner() = productApi.getHomeBanner()
    suspend fun getBrand() = productApi.getBrand()

    suspend fun getThumbnailByIds(ids: List<Int>) = productApi.getThumbnailByIds(ids)

    suspend fun markAsViewed(productId: Int) {
        localRepository.insertRecentlyViewed(RecentlyViewedRealm().apply { this.productId = productId })
    }

    suspend fun getAllRecentlyViewedFlow(): Flow<List<RecentlyViewedRealm>> {
        return localRepository.selectAllRecentlyViewed()
    }
}