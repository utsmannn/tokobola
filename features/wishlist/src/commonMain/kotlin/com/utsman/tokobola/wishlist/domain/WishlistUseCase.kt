package com.utsman.tokobola.wishlist.domain

import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.StateTransformation

class WishlistUseCase(private val repository: WishlistRepository) {

    val productWishlistReducer = ApiReducer<List<ThumbnailProduct>>()

    suspend fun getWishlist() {
        repository.getAllWishlist()
            .collect { realms ->
                productWishlistReducer.transform(
                    transformation = StateTransformation.SimpleTransform(),
                    call = {
                        val ids = realms.map { it.productId }
                        repository.getThumbnailByIds(ids)
                    },
                    mapper = { thumbnailProductResponse ->
                        thumbnailProductResponse.data
                            ?.map { it.toThumbnailProduct() }
                            .orEmpty()
                    }
                )
            }
    }
}