package com.utsman.tokobola.common.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.utsman.tokobola.core.data.LatLon
import kotlinx.cinterop.copy
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun MapView(mapConfigState: MapConfigState, modifier: Modifier) {
    val latLon = mapConfigState.currentLatLon
    val location = CLLocationCoordinate2DMake(latLon.latitude, latLon.longitude)

    val mkMapView = remember {
        MKMapView().also {

            val region = MKCoordinateRegionMakeWithDistance(
                centerCoordinate = location,
                10_000.0, 10_000.0
            )
            it.setRegion(
                region,
                animated = true
            )

            mapConfigState.mapAction = object : MapAction {
                override fun getCenterLatLon(): LatLon {
                    return it.centerCoordinate().useContents { LatLon(latitude, longitude) }
                }

                override fun zoomIn() {
                    val newRegion = it.region().copy {
                        center.latitude = getCenterLatLon().latitude
                        center.longitude = getCenterLatLon().longitude
                        span.latitudeDelta *= 0.5
                        span.longitudeDelta *= 0.5
                    }

                    it.setRegion(newRegion, true)
                }

                override fun zoomOut() {
                    val newRegion = it.region().copy {
                        center.latitude = getCenterLatLon().latitude
                        center.longitude = getCenterLatLon().longitude
                        span.latitudeDelta *= 2.0
                        span.longitudeDelta *= 2.0
                    }

                    it.setRegion(newRegion, true)
                }

                override fun setLocation(latLon: LatLon) {
                    val newRegion = it.region().copy {
                        center.latitude = latLon.latitude
                        center.longitude = latLon.longitude
                    }

                    it.setRegion(newRegion, true)
                }
            }
        }
    }

    DisposableEffect(mkMapView) {
        onDispose {
            mapConfigState.mapAction = null
        }
    }

    UIKitView(
        modifier = modifier.onDrag {
            val currentCoordinate = mkMapView.centerCoordinate()
            val updatedLatLon = currentCoordinate.useContents {
                LatLon(latitude, longitude)
            }

            mapConfigState.currentLatLon = updatedLatLon
        },
        factory = {
            mkMapView
        },
        update = {

        }
    )
}