package com.utsman.tokobola.api

fun productWebApi(): Lazy<ProductWebApi> {
    return lazy(
        mode = LazyThreadSafetyMode.SYNCHRONIZED
    ) {
        ProductWebApi.create { ProductWebApi() }
    }
}