package com.utsman.tokobola.home.domain

import com.utsman.tokocot.api.productWebApi

class HomeRepository {

    private val productApi by productWebApi()

    suspend fun getProductPaged(page: Int) = productApi.getFeaturedPaged(page)
    suspend fun getBanner() = productApi.getHomeBanner()
    suspend fun getBrand() = productApi.getBrand()
}