package com.utsman.tokobola.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.toCameraOptions
import com.utsman.tokobola.common.BuildKonfig
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.core.data.orNol

private typealias MapBoxView = com.mapbox.maps.MapView

@Composable
actual fun MapView(mapConfigState: MapConfigState, modifier: Modifier) {
    val context = LocalContext.current

    //val annotations by mutableStateOf(mutableListOf<MKPointAnnotation>())

    val mapboxView = remember {
        val resourcesOptions = ResourceOptions
            .Builder()
            .accessToken(BuildKonfig.MAPBOX_TOKEN)
            .build()

        val option = MapInitOptions(context, resourcesOptions)

        MapBoxView(context, option).also {
            it.getMapboxMap()
                .setCamera(
                    CameraOptions.Builder()
                        .center(
                            Point.fromLngLat(mapConfigState.currentLatLon.longitude, mapConfigState.currentLatLon.latitude)
                        )
                        .zoom(12.0)
                        .build()
                )

            mapConfigState.mapAction = object : MapAction {
                override fun getCenterLatLon(): LatLon {
                    val center = it.getMapboxMap().cameraState.center
                    return LatLon(latitude = center.latitude(), longitude = center.longitude())
                }

                override fun zoomIn() {
                    val currentOption = it.getMapboxMap().cameraState
                        .toCameraOptions()
                    val cameraOption = currentOption
                        .toBuilder()
                        .zoom(currentOption.zoom.orNol() + 2.0)
                        .build()

                    it.getMapboxMap().flyTo(cameraOption)
                }

                override fun zoomOut() {
                    val currentOption = it.getMapboxMap().cameraState
                        .toCameraOptions()
                    val cameraOption = currentOption
                        .toBuilder()
                        .zoom(currentOption.zoom.orNol() - 2.0)
                        .build()

                    it.getMapboxMap().flyTo(cameraOption)
                }

                override fun setLocation(latLon: LatLon) {
                    val cameraOption = it.getMapboxMap().cameraState
                        .toCameraOptions()
                        .toBuilder()
                        .center(Point.fromLngLat(latLon.longitude, latLon.latitude))
                        .build()

                    it.getMapboxMap().flyTo(cameraOption)
                }

                override fun addAnnotation(latLon: LatLon, title: String?) {
                    val annotationPlugin = it.annotations
                    val manager = annotationPlugin.createPointAnnotationManager()
                    manager.deleteAll()

                    val pointAnnotation = PointAnnotationOptions()
                        .withPoint(Point.fromLngLat(latLon.longitude, latLon.latitude))

                    manager.create(pointAnnotation)
                }
            }
        }
    }

    AndroidView(
        factory = {
            mapboxView
        }
    )
}