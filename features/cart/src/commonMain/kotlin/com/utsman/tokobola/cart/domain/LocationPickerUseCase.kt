package com.utsman.tokobola.cart.domain

import androidx.compose.runtime.derivedStateOf
import com.utsman.tokobola.common.entity.LocationPlace
import com.utsman.tokobola.common.toLocationPlace
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.location.LocationTrackerProvider
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.StateTransformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.takeWhile

class LocationPickerUseCase(private val repository: CartRepository) {

    val query = MutableStateFlow("")

    val locationSearchReducer = ApiReducer<List<LocationPlace>>()
    val locationReverseReducer = ApiReducer<LocationPlace>()

    suspend fun searchLocationPlace(query: String, latLon: LatLon) {
        if (this.query.value != query) {
            this.query.value = query
        }

        when {
            (query.count() < 3) -> {
                this.query.value = ""
            }
            else -> {
                locationSearchReducer
                    .transform(
                        transformation = StateTransformation.SimpleTransform(),
                        call = {
                            repository.searchLocationPlace(query, latLon)
                        },
                        mapper = { response ->
                            response.features.orEmpty()
                                .mapNotNull { it?.toLocationPlace() }
                        }
                    )
            }
        }
    }

    suspend fun getLocationReverse(latLon: LatLon) {
        locationReverseReducer.transform(
            transformation = StateTransformation.SimpleTransform(),
            call = {
                repository.getLocationPlace(latLon)
            },
            mapper = {
                it.toLocationPlace()
            }
        )
    }

    fun clearData() {
        locationSearchReducer.clear()
        locationReverseReducer.clear()
        query.value = ""
    }

    companion object : SingletonCreator<LocationPickerUseCase>()
}