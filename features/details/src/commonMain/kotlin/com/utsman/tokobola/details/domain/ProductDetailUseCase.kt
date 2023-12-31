package com.utsman.tokobola.details.domain

import com.utsman.tokobola.common.entity.CartProduct
import com.utsman.tokobola.common.mapToProduct
import com.utsman.tokobola.common.entity.Product
import com.utsman.tokobola.common.toEntity
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.network.ApiReducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ProductDetailUseCase(private val repository: DetailRepository) {

    val productDetailReducer = ApiReducer<Product>()

    val productCart = MutableStateFlow(CartProduct())

    val isProductExist = MutableStateFlow(false)

    suspend fun getDetail(productId: Int) {
        productDetailReducer.transform(
            call = { repository.getDetailProduct(productId) },
            mapper = {
                it.data?.mapToProduct() ?: Product()
            }
        )
    }

    suspend fun markProductViewed(productId: Int) {
        repository.markAsViewed(productId)
    }

    suspend fun incrementCart(productId: Int) {
        repository.insertOrUpdateCart(productId) {
            it + 1
        }
    }

    suspend fun decrementCart(productId: Int) {
        repository.insertOrUpdateCart(productId) {
            it - 1
        }
    }

    suspend fun getProductCart(productId: Int) {
        repository.getCartProduct(productId)
            .map { it?.toEntity() ?: CartProduct() }
            .collect {
                productCart.value = it
            }
    }

    suspend fun listenIsWishlistExist(productId: Int) {
        repository.isWishlistExist(productId)
            .collect { isExist ->
                isProductExist.value = isExist
            }
    }

    suspend fun toggleWishlist(productId: Int) {
        repository.toggleWishlist(productId)
    }

    suspend fun clearDetail() {
        productDetailReducer.clear()
    }

    companion object : SingletonCreator<ProductDetailUseCase>()
}