package com.utsman.tokobola.home.domain

class HomeRepository(private val homeDataSources: HomeDataSources) {

    suspend fun getProductPaged(page: Int) = homeDataSources.getProductPaged(page)
    suspend fun getBanner() = homeDataSources.getBanner()
}