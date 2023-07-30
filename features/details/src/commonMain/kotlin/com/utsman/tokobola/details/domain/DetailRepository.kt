package com.utsman.tokobola.details.domain

import com.utsman.tokobola.api.productWebApi

class DetailRepository {

    private val productApi by productWebApi()

    suspend fun getDetailProduct(productId: Int) = productApi.getDetail(productId)
}