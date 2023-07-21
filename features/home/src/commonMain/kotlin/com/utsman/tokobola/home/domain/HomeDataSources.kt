package com.utsman.tokobola.home.domain

import com.utsman.tokobola.network.NetworkSources
import com.utsman.tokobola.network.response.BasePagedResponse

class HomeDataSources : NetworkSources("https://footballstore.fly.dev/api") {

    init {
        println("created............")
    }

    suspend fun getProductPaged(): BasePagedResponse<ProductResponse> {
        return getPaged("/product")
    }
}