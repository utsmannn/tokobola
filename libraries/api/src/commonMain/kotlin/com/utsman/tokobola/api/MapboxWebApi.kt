package com.utsman.tokobola.api

import com.utsman.tokobola.api.response.MapboxReverseResponse
import com.utsman.tokobola.api.response.MapboxSearchResponse
import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.data.LatLon
import com.utsman.tokobola.core.utils.calculateBboxMapbox
import com.utsman.tokobola.network.NetworkSources

class MapboxWebApi : NetworkSources(BuildKonfig.MAPBOX_BASE_URL) {


    suspend fun getReverseGeocoding(latLon: LatLon, mapboxAccessToken: String): MapboxReverseResponse {
        return getRaw(
            endPoint = WebEndPoint.MAPBOX_GEOCODING
                .withParam("lat", latLon.latitude)
                .withParam("lon", latLon.longitude)
                .withParam("mapbox_access_token", mapboxAccessToken)
        )
    }

    suspend fun getSearchGeocoding(query: String, latLon: LatLon, mapboxAccessToken: String): MapboxSearchResponse {
        return getRaw(
            endPoint = WebEndPoint.MAPBOX_SEARCH
                .withParam("proximity", "${latLon.longitude},${latLon.latitude}")
                .withParam("q", query)
                .withParam("mapbox_access_token", mapboxAccessToken)
        )
    }

    companion object : SingletonCreator<MapboxWebApi>()
}