package com.utsman.tokobola.core.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

data class LatLon(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    fun json(): String {
        return "{\"latitude\": \"$latitude\", \"longitude\":\"$longitude\"}"
    }

    fun isBlank(): Boolean {
        return latitude == 0.0
    }

    companion object {
        fun fromJson(json: String): LatLon {
            val element = Json.parseToJsonElement(json)
            return Json.decodeFromJsonElement(element)
        }
    }
}