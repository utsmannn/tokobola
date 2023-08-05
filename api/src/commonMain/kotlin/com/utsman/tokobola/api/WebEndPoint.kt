package com.utsman.tokobola.api

object WebEndPoint {

    const val PRODUCT_FEATURED = "/v2/product/featured?page={page}"
    const val PRODUCT_BRAND = "/v2/product/brand/{brand_id}?page={page}"
    const val PRODUCT_CATEGORY = "/v2/product/category/{category_id}?page={page}"
    const val PRODUCT_TOP = "/v2/product/top"
    const val PRODUCT_CURATED = "/v2/product/curated"
    const val PRODUCT_DETAIL = "/v2/product/{product_id}"
    const val BANNER = "/v2/product/banner"
    const val BRAND = "/brand"
    const val CATEGORY = "/category"
}

internal fun String.withParam(key: String, value: Any): String {
    return replace("{$key}", "$value")
}