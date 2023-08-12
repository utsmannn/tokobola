package com.utsman.tokobola.wishlist.domain

import com.utsman.tokobola.api.productWebApi
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.database.localRepository

class WishlistRepository {

    private val productWebApi by productWebApi()
    private val localRepository by localRepository()

    suspend fun getThumbnailByIds(ids: List<Int>) = productWebApi.getThumbnailByIds(ids)

    suspend fun getAllWishlist() = localRepository.selectAllWishlist()

    companion object : SingletonCreator<WishlistRepository>()
}