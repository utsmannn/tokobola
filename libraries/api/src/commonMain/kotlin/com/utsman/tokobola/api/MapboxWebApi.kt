package com.utsman.tokobola.api

import com.utsman.tokobola.api.response.MapboxGeocodingResponse
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.network.NetworkSources

class MapboxWebApi : NetworkSources(BuildKonfig.MAPBOX_BASE_URL) {


    suspend fun getGeocodingLatLon(latLon: LatLon, mapboxAccessToken: String): MapboxGeocodingResponse {
        return getRaw(
            endPoint = WebEndPoint.MAPBOX_GEOCODING
                .withParam("lat", latLon.latitude)
                .withParam("lon", latLon.longitude)
                .withParam("mapbox_access_token", mapboxAccessToken)
        )
    }

    companion object : SingletonCreator<MapboxWebApi>()
}