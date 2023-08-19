package com.utsman.tokobola.common.entity

import com.utsman.tokobola.core.data.LatLon

data class LocationPlace(
    val name: String = "",
    val latLon: LatLon = LatLon(),
    val bbox: String = ""
)
