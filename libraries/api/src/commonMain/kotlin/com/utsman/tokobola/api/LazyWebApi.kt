package com.utsman.tokobola.api

fun productWebApi(): Lazy<ProductWebApi> {
    return lazy(
        mode = LazyThreadSafetyMode.SYNCHRONIZED
    ) {
        ProductWebApi.create { ProductWebApi() }
    }
}

fun mapboxWebApi(): Lazy<MapboxWebApi> {
    return lazy(
        mode = LazyThreadSafetyMode.SYNCHRONIZED
    ) {
        MapboxWebApi.create { MapboxWebApi() }
    }
}