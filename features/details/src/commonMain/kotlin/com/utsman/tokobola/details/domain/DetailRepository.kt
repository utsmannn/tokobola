package com.utsman.tokobola.details.domain

import com.utsman.tokobola.api.productWebApi
import com.utsman.tokobola.common.entity.CartProduct
import com.utsman.tokobola.database.data.RecentlyViewedRealm
import com.utsman.tokobola.database.localRepository
import kotlinx.coroutines.flow.Flow

class DetailRepository {

    private val productApi by productWebApi()
    private val localRepository by localRepository()

    suspend fun getDetailProduct(productId: Int) = productApi.getDetail(productId)

    suspend fun markAsViewed(productId: Int) {
        localRepository.insertRecentlyViewed(RecentlyViewedRealm().apply { this.productId = productId })
    }

    suspend fun getCartProduct(productId: Int) = localRepository.getProductCart(productId)

    suspend fun insertOrUpdateCart(productId: Int, operationQuantity: (Int) -> Int) {
        localRepository.insertOrUpdateProductCart(productId, operationQuantity)
    }

    suspend fun getProductCategory(categoryId: Int, page: Int) = productApi.getByCategoryPaged(categoryId, page)
    suspend fun getProductBrand(brandId: Int, page: Int) = productApi.getByBrandPaged(brandId, page)
}