package com.utsman.tokobola.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.location.LocalLocationTrackerProvider
import dev.icerock.moko.geo.LatLng
import kotlinx.serialization.json.Json

@Composable
expect fun MapView(
    mapConfigState: MapConfigState = rememberMapConfigState(),
    modifier: Modifier = Modifier
)

data class MapConfig(
    val currentLatLon: LatLon = LatLon()
)

internal interface MapAction {
    fun getCenterLatLon(): LatLon
    fun zoomIn()
    fun zoomOut()
    fun setLocation(latLon: LatLon)
}

fun LatLng.toLatLon(): LatLon {
    return LatLon(latitude, longitude)
}

class MapConfigState(currentLatLon: LatLon) {
    var currentLatLon: LatLon by mutableStateOf(currentLatLon)

    internal var mapAction: MapAction? = null

    fun getCenterLocation(): LatLon {
        return mapAction?.getCenterLatLon() ?: LatLon()
    }

    fun zoomIn() = mapAction?.zoomIn()
    fun zoomOut() = mapAction?.zoomOut()

    fun setLocation(latLon: LatLon) = mapAction?.setLocation(latLon)

    companion object {
        val Saver: Saver<MapConfigState, String> = Saver(
            save = {
                it.currentLatLon.json()
            },
            restore = {
                MapConfigState(LatLon.fromJson(it))
            }
        )
    }
}

@Composable
fun rememberMapConfigState(latLon: LatLon? = null): MapConfigState {

    val state = if (latLon == null) {
        val trackerProvider = LocalLocationTrackerProvider.current
        val location = trackerProvider.locationFlow.collectAsState()
        LaunchedEffect(trackerProvider) {
            if (!trackerProvider.isHasStart) {
                trackerProvider.startTracking()
            }
        }

        val newLatLon by derivedStateOf {
            location.value?.toLatLon() ?: LatLon()
        }

        MapConfigState(newLatLon)
    } else {
        MapConfigState(latLon)
    }

    return rememberSaveable(saver = MapConfigState.Saver) { state }
}