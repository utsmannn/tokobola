package com.utsman.tokobola.explore.domain

import com.utsman.tokobola.api.productWebApi

class ExploreRepository {
    private val productApi by productWebApi()

    suspend fun getBrand() = productApi.getBrand()
    suspend fun getCategory() = productApi.getCategory()
    suspend fun getProductBrand(brandId: Int, page: Int) = productApi.getByBrandPaged(brandId, page)
    suspend fun getProductCategory(categoryId: Int, page: Int) = productApi.getByCategoryPaged(categoryId, page)
    suspend fun getProductTop() = productApi.getTop()
    suspend fun getProductCurated() = productApi.getCurated()
}