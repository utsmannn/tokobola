package com.utsman.tokobola.api

object WebEndPoint {

    const val PRODUCT_FEATURED = "/v2/product/featured?page={page}"
    const val PRODUCT_BRAND = "/v2/product/brand/{brand_id}?page={page}"
    const val PRODUCT_CATEGORY = "/v2/product/category/{category_id}?page={page}"
    const val PRODUCT_SEARCH = "/v2/product/search?q={query}&page={page}"
    const val PRODUCT_THUMBNAIL = "/v2/product/thumbnail?id={id}"
    const val PRODUCT_TOP = "/v2/product/top"
    const val PRODUCT_CURATED = "/v2/product/curated"
    const val PRODUCT_DETAIL = "/v2/product/{product_id}"
    const val BANNER = "/v2/product/banner"
    const val BRAND = "/brand"
    const val CATEGORY = "/category"

    const val MAPBOX_GEOCODING = "/geocoding/v5/mapbox.places/{lon},{lat}.json?access_token={mapbox_access_token}"
    const val MAPBOX_SEARCH = "/geocoding/v5/mapbox.places/{q}.json?access_token={mapbox_access_token}&proximity={proximity}"
}

internal fun String.withParam(key: String, value: Any): String {
    return replace("{$key}", "$value")
}