package com.utsman.tokobola.api

object WebEndPoint {

    const val PRODUCT_FEATURED = "/v2/product/featured?page={page}"
    const val PRODUCT_DETAIL = "/v2/product/{product_id}"
    const val PRODUCT_BRAND = "/v2/product/brand/{brand_id}?page={page}"
    const val BANNER = "/v2/product/banner"
    const val BRAND = "/brand"
}

internal fun String.withParam(key: String, value: Any): String {
    return replace("{$key}", "$value")
}