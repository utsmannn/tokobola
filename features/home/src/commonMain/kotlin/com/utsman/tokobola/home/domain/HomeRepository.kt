package com.utsman.tokobola.home.domain

class HomeRepository(private val homeDataSources: HomeDataSources) {

    suspend fun getProductPaged() = homeDataSources.getProductPaged()
    suspend fun getBanner() = homeDataSources.getBanner()
}