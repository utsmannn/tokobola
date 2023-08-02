package com.utsman.tokobola.home.domain

import com.utsman.tokobola.api.productWebApi

class HomeRepository {

    private val productApi by productWebApi()

    suspend fun getFeaturedProductPaged(page: Int) = productApi.getByFeaturedPaged(page)
    suspend fun getBanner() = productApi.getHomeBanner()
    suspend fun getBrand() = productApi.getBrand()
}