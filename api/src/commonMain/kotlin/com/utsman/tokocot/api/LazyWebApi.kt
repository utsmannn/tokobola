package com.utsman.tokocot.api

fun productWebApi(): Lazy<ProductWebApi> {
    return lazy(
        mode = LazyThreadSafetyMode.SYNCHRONIZED
    ) {
        ProductWebApi.getInstance()
    }
}