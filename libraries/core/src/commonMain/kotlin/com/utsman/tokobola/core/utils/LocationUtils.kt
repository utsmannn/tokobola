package com.utsman.tokobola.core.utils

import com.utsman.tokobola.core.data.LatLon
import kotlin.math.cos

fun calculateBboxMapbox(center: LatLon, distanceKm: Double): String {
    val latRadian = center.latitude.radian()
    val lonRadian = center.longitude.radian()
    val latOffset = distanceKm / EARTH_RADIUS

    // Calculate latitude range
    val minLat =(latRadian - latOffset).degrees()
    val maxLat = (latRadian + latOffset).degrees()

    // Calculate longitude range
    val lonOffset = distanceKm / (EARTH_RADIUS * cos(latRadian))
    val minLon = (lonRadian - lonOffset).degrees()
    val maxLon = (lonRadian + lonOffset).degrees()

    return "$minLon,$minLat,$maxLon,$maxLat"
}

private fun Double.radian(): Double {
    return this * PI / 180.0
}

private fun Double.degrees(): Double {
    return this * 180.0 / PI
}

private const val PI = 3.14159265358979323846
private const val EARTH_RADIUS = 6371.0
