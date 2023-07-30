package com.utsman.tokobola.home.domain

class HomeRepository(private val homeDataSources: HomeDataSources) {

    suspend fun getProductPaged(page: Int) = homeDataSources.getFeaturedProductPaged(page)
    suspend fun getBanner() = homeDataSources.getBanner()
    suspend fun getBrand() = homeDataSources.getBrand()
}