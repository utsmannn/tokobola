package com.utsman.tokobola.details.domain

import com.utsman.tokobola.api.productWebApi
import com.utsman.tokobola.database.data.RecentlyViewedRealm
import com.utsman.tokobola.database.localRepository
import kotlinx.coroutines.flow.Flow

class DetailRepository {

    private val productApi by productWebApi()
    private val localRepository by localRepository()

    suspend fun getDetailProduct(productId: Int) = productApi.getDetail(productId)

    suspend fun getThumbnailByIds(ids: List<Int>) = productApi.getThumbnailByIds(ids)

    suspend fun markAsViewed(productId: Int) {
        localRepository.insertRecentlyViewed(RecentlyViewedRealm().apply { this.productId = productId })
    }

    suspend fun getAllRecentlyViewedFlow(): Flow<List<RecentlyViewedRealm>> {
        return localRepository.selectAllRecentlyViewed()
    }
}