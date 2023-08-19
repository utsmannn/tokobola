package com.utsman.tokobola.cart.domain

import com.utsman.tokobola.api.mapboxWebApi
import com.utsman.tokobola.api.productWebApi
import com.utsman.tokobola.cart.BuildKonfig
import com.utsman.tokobola.cart.ui.CartUiConfig
import com.utsman.tokobola.common.entity.Cart
import com.utsman.tokobola.common.entity.LocationPlace
import com.utsman.tokobola.common.toRealm
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.database.data.CartProductRealm
import com.utsman.tokobola.database.localRepository

class CartRepository {
    private val productWebApi by productWebApi()
    private val mapboxWebApi by mapboxWebApi()
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

    suspend fun getLocationPlace(latLon: LatLon) = mapboxWebApi.getGeocodingLatLon(latLon, BuildKonfig.MAPBOX_TOKEN)

    suspend fun getLocalCurrentLocationPlace() = localRepository.getLocationPlace(CartUiConfig.KEY_LOCATION_CURRENT)
    suspend fun getShippingLocationPlace() = localRepository.getLocationPlace(CartUiConfig.KEY_LOCATION_SHIPPING)
    suspend fun insertLocalCurrentLocationPlace(locationPlace: LocationPlace) {
        localRepository.insertOrUpdateLocationPlace(locationPlace.toRealm(CartUiConfig.KEY_LOCATION_CURRENT))
    }

    suspend fun insertShippingLocationPlace(locationPlace: LocationPlace) {
        localRepository.insertOrUpdateLocationPlace(locationPlace.toRealm(CartUiConfig.KEY_LOCATION_SHIPPING))
    }

    companion object : SingletonCreator<CartRepository>()
}