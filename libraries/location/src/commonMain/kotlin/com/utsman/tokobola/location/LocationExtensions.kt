package com.utsman.tokobola.location

import com.utsman.tokobola.core.data.LatLon
import dev.icerock.moko.geo.LatLng

fun LatLon.isNear(other: LatLon): Boolean {
    return LatLng(latitude, longitude).distanceTo(LatLng(other.latitude, other.longitude)) < 30.0
}