package com.utsman.tokobola.cart.domain

import com.utsman.tokobola.common.entity.Cart
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.database.data.CartProductRealm
import com.utsman.tokobola.network.ApiReducer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.transform

class CartUseCase(private val repository: CartRepository) {


    val cartReducer = ApiReducer<List<Cart>>()

    suspend fun getCart() {
        repository.getAllCart()
            .collect { realms ->
                if (realms.isNotEmpty()) {
                    cartReducer.transform(
                        call = {
                            repository.getThumbnailByIds(realms.map { it.productId })
                        },
                        mapper = { response ->
                            val data = response.data.orEmpty().map { it.toThumbnailProduct() }
                            data.mapIndexed { index, thumbnailProduct ->
                                Cart(thumbnailProduct, realms[index].quantity, realms[index].millis)
                            }
                        }
                    )
                }
            }
    }

   suspend fun updateCart(list: List<Cart>) {
       repository.replaceCart(list)
   }

    companion object : SingletonCreator<CartUseCase>()
}