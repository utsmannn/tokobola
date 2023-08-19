package com.utsman.tokobola.cart.domain

import com.utsman.tokobola.common.entity.LocationPlace
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.location.LocationTrackerProvider
import com.utsman.tokobola.network.ApiReducer
import kotlinx.coroutines.flow.MutableStateFlow

class LocationPickerUseCase(private val locationTrackerProvider: LocationTrackerProvider) {

    val query = MutableStateFlow("")
    private var currentQuery = ""

    val locationState = locationTrackerProvider.locationStateFlow

    suspend fun getLocation() {
        locationTrackerProvider.startTracking()
    }

    suspend fun stopLocation() {
        locationTrackerProvider.stopTracking()
    }

    companion object : SingletonCreator<LocationPickerUseCase>()
}