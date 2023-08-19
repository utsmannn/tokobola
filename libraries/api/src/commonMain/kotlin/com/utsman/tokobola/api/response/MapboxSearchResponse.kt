package com.utsman.tokobola.api.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapboxSearchResponse(
    @SerialName("type")
    val type: String?,
    @SerialName("query")
    val query: List<String?>?,
    @SerialName("features")
    val features: List<FeatureResponse?>?,
    @SerialName("attribution")
    val attribution: String?
) {
    @Serializable
    data class FeatureResponse(
        @SerialName("id")
        val id: String?,
        @SerialName("type")
        val type: String?,
        @SerialName("place_type")
        val placeType: List<String?>?,
        @SerialName("relevance")
        val relevance: Double?,
        @SerialName("properties")
        val properties: PropertiesResponse?,
        @SerialName("text")
        val text: String?,
        @SerialName("place_name")
        val placeName: String?,
        @SerialName("center")
        val center: List<Double?>?,
        @SerialName("geometry")
        val geometry: GeometryResponse?,
        @SerialName("context")
        val context: List<ContextResponse?>?
    ) {
        @Serializable
        data class PropertiesResponse(
            @SerialName("foursquare")
            val foursquare: String?,
            @SerialName("landmark")
            val landmark: Boolean?,
            @SerialName("address")
            val address: String?,
            @SerialName("category")
            val category: String?
        )

        @Serializable
        data class GeometryResponse(
            @SerialName("coordinates")
            val coordinates: List<Double?>?,
            @SerialName("type")
            val type: String?
        )

        @Serializable
        data class ContextResponse(
            @SerialName("id")
            val id: String?,
            @SerialName("mapbox_id")
            val mapboxId: String?,
            @SerialName("text")
            val text: String?,
            @SerialName("wikidata")
            val wikidata: String?,
            @SerialName("short_code")
            val shortCode: String?
        )
    }
}