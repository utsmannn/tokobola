package com.utsman.tokobola.details.domain

class DetailRepository(private val dataSources: DetailDataSources) {

    suspend fun getDetailProduct(productId: Int) = dataSources.getDetail(productId)
}