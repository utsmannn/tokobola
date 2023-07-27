package com.utsman.tokobola.details.domain

import com.utsman.tokobola.common.entity.response.ProductResponse
import com.utsman.tokobola.network.NetworkSources
import com.utsman.tokobola.network.response.BaseResponse

class DetailDataSources : NetworkSources("https://footballstore.fly.dev/api") {

    init {
        println("created detail data sources............")
    }

    suspend fun getDetail(productId: Int): BaseResponse<ProductResponse> =
        get("/product/$productId")
}