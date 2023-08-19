package com.utsman.tokobola.cart.domain

import com.utsman.tokobola.common.entity.Cart
import com.utsman.tokobola.common.entity.LocationPlace
import com.utsman.tokobola.common.toLocationPlace
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.State
import com.utsman.tokobola.location.LocationTrackerProvider
import com.utsman.tokobola.location.isNear
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.StateTransformation
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CartUseCase(
    private val repository: CartRepository,
    private val locationTrackerProvider: LocationTrackerProvider
) {


    val cartReducer = ApiReducer<List<Cart>>()

    val locationPlaceReducer = ApiReducer<LocationPlace>()
    val locationShippingReducer = ApiReducer<LocationPlace>()

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

    suspend fun getShippingLocation() {
        val currentLocation = repository.getShippingLocationPlace()
            .map {
                it?.toLocationPlace()
            }
            .firstOrNull()

        if (currentLocation == null) {
            val currentLocalLocation = repository.getLocalCurrentLocationPlace()
                .map {
                    it?.toLocationPlace()
                }
                .firstOrNull() ?: LocationPlace()

            locationTrackerProvider.startTracking()
            locationTrackerProvider
                .locationFlow
                .onStart {
                    locationShippingReducer.forcePushState(State.Loading())
                }
                .filterNotNull()
                .collect { latLon ->
                    locationShippingReducer
                        .transform(
                            transformation = StateTransformation.SimpleTransform(),
                            call = {
                                if (currentLocalLocation.latLon.isNear(latLon)) {
                                    currentLocalLocation
                                } else {
                                    repository.getLocationPlace(latLon).toLocationPlace().also { locationPlace ->
                                        repository.insertLocalCurrentLocationPlace(locationPlace)
                                        repository.insertShippingLocationPlace(locationPlace)
                                    }
                                }
                            },
                            mapper = {
                                it
                            }
                        )
                }
        } else {
            locationShippingReducer
                .transform(
                    transformation = StateTransformation.SimpleTransform(),
                    call = {
                        currentLocation
                    },
                    mapper = {
                        it
                    }
                )
        }
    }



    fun stopLocationPlace() {
        locationTrackerProvider.stopTracking()
    }

    suspend fun updateCart(list: List<Cart>) {
        repository.replaceCart(list)
    }

    companion object : SingletonCreator<CartUseCase>()
}