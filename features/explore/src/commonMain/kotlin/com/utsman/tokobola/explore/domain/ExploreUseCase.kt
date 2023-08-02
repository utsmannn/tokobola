package com.utsman.tokobola.explore.domain

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toBrand
import com.utsman.tokobola.common.toCategory
import com.utsman.tokobola.common.toHomeProduct
import com.utsman.tokobola.network.ApiReducer

class ExploreUseCase(private val repository: ExploreRepository) {

    val brandReducer = ApiReducer<List<Brand>>()
    val categoryReducer = ApiReducer<List<Category>>()
    val productCategory = ApiReducer<List<ThumbnailProduct>>()

    suspend fun getBrand() {
        brandReducer.transform(
            call = {
                repository.getBrand()
            },
            mapper = { brandResponse ->
                brandResponse.data?.map { it.toBrand() }.orEmpty()
                    .filter {
                        // filter "other" brand
                        it.id != 40
                    }
            }
        )
    }

    suspend fun getCategory() {
        categoryReducer.transform(
            call = {
                repository.getCategory()
            },
            mapper = { categoryResponse ->
                categoryResponse.data?.map { it.toCategory() }.orEmpty()
                    .filter {
                        // filter "other" brand
                        it.id != 40
                    }
            }
        )
    }

    suspend fun getProductCategory(categoryId: Int) {
        // get page 1 on product
        productCategory.transform(
            call = {
                repository.getProductCategory(categoryId, 1)
            },
            mapper = { productResponse ->
                val dataResponse = productResponse.data?.data.orEmpty()
                dataResponse.map {
                    it.toHomeProduct()
                }
            }
        )
    }

    fun clearData() {
        brandReducer.clear()
        categoryReducer.clear()
    }
}