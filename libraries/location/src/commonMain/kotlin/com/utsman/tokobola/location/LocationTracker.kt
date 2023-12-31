package com.utsman.tokobola.location

import androidx.compose.runtime.Composable
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.core.utils.DefaultScope
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform

internal expect val locationTracker: LocationTracker

private var lastKnowLocations: MutableList<LatLon> = mutableListOf()

class LocationTrackerProvider {
    private val tracker = locationTracker

    var isHasStart: Boolean = false
        private set

    val locationFlow = tracker.getLocationsFlow()
        .map { LatLon(it.latitude, it.longitude) }
        .onEach {
            if (lastKnowLocations.isEmpty()) {
                lastKnowLocations.add(it)
            }
        }
        .stateIn(
            DefaultScope(),
            SharingStarted.Eagerly,
            null
        )

    val locationStateFlow = locationFlow
        .transform { value ->
            if (value == null) {
                emit(State.Loading())
            } else {
                emit(State.Success(value))
            }
        }
        .stateIn(
            DefaultScope(),
            SharingStarted.Eagerly,
            State.Idle()
        )

    @Composable
    fun bindComposable() {
        BindLocationTrackerEffect(tracker)
        BindEffect(tracker.permissionsController)
    }

    fun getLastKnownLocation(): LatLon? {
        return lastKnowLocations.firstOrNull()
    }

    suspend fun startTracking() {
        if (!isHasStart) {
            tracker.startTracking()
            isHasStart = true
        }
    }

    fun stopTracking() {
        tracker.stopTracking()
        isHasStart = false
    }

    suspend fun isEmptyQueue(): Boolean {
        return tracker.getLocationsFlow().firstOrNull() == null
    }

    companion object : SingletonCreator<LocationTrackerProvider>()
}