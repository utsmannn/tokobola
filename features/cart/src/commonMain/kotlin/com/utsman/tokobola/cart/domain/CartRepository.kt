package com.utsman.tokobola.cart.domain

import com.utsman.tokobola.api.productWebApi
import com.utsman.tokobola.common.entity.Cart
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.database.data.CartProductRealm
import com.utsman.tokobola.database.localRepository

class CartRepository {
    private val productWebApi by productWebApi()
    private val localRepository by localRepository()

    suspend fun getThumbnailByIds(ids: List<Int>) = productWebApi.getThumbnailByIds(ids)

    suspend fun getAllCart() = localRepository.selectAllCart()

    suspend fun replaceCart(list: List<Cart>) {
        val cartProductRealm = list.map {
            CartProductRealm().apply {
                productId = it.product.id
                quantity = it.quantity
                millis = it.millis
            }
        }

        localRepository.replaceAllCart(cartProductRealm)
    }

    companion object : SingletonCreator<CartRepository>()
}