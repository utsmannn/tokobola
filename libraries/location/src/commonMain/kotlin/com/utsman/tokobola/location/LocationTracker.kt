package com.utsman.tokobola.location

import androidx.compose.runtime.Composable
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.utils.DefaultScope
import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

internal expect val locationTracker: LocationTracker

class LocationTrackerProvider {
    private val tracker = locationTracker

    var isHasStart: Boolean = false
        private set

    val locationFlow = tracker.getLocationsFlow()
        .stateIn(
            DefaultScope(),
            SharingStarted.Eagerly,
            null
        )

    suspend fun getLastLocation(): LatLng? {
        return locationFlow.firstOrNull()
    }

    fun getLastLocationBlocking(): LatLng? {
        return runBlocking { locationFlow.firstOrNull() }
    }

    @Composable
    fun bindComposable() {
        BindLocationTrackerEffect(tracker)
        BindEffect(tracker.permissionsController)
    }

    suspend fun startTracking() {
        tracker.startTracking()
        isHasStart = true
    }

    fun stopTracking() {
        tracker.stopTracking()
        isHasStart = false
    }

    companion object : SingletonCreator<LocationTrackerProvider>()
}