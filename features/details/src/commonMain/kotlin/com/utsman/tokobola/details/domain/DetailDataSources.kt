package com.utsman.tokobola.details.domain

import com.utsman.tokobola.network.NetworkSources
import com.utsman.tokobola.network.response.BaseResponse

class DetailDataSources : NetworkSources("https://footballstore.fly.dev/api") {

    init {
        println("created detail data sources............")
    }

    suspend fun getDetail(productId: Int): BaseResponse<ProductDetailResponse> =
        get("/product/$productId")
}