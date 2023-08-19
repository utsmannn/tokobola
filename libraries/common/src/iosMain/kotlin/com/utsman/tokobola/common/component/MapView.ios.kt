package com.utsman.tokobola.common.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.utsman.tokobola.core.data.LatLon
import kotlinx.cinterop.copy
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun MapView(mapConfigState: MapConfigState, modifier: Modifier) {
    val latLon = mapConfigState.currentLatLon
    val location = CLLocationCoordinate2DMake(latLon.latitude, latLon.longitude)

    val annotations by mutableStateOf(mutableListOf<MKPointAnnotation>())

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
                    println("asuuu set location -> $latLon")
                    val newRegion = it.region().copy {
                        center.latitude = latLon.latitude
                        center.longitude = latLon.longitude
                    }

                    it.setRegion(newRegion, true)
                }

                override fun addAnnotation(latLon: LatLon, title: String?) {
                    val annotationLocation = CLLocationCoordinate2DMake(latLon.latitude, latLon.longitude)
                    val annotation = MKPointAnnotation(
                        coordinate = annotationLocation,
                        title = title,
                        subtitle = null
                    )

                    if (annotations.isNotEmpty()) {
                        it.removeAnnotations(annotations)
                    }
                    it.addAnnotation(annotation)
                    annotations.add(annotation)
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
        modifier = modifier,
        factory = {
            mkMapView
        }
    )
}