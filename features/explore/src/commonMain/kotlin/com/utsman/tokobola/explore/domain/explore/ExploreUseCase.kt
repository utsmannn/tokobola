package com.utsman.tokobola.explore.domain.explore

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toBrand
import com.utsman.tokobola.common.toCategory
import com.utsman.tokobola.common.toThumbnailProduct
import com.utsman.tokobola.explore.domain.ExploreRepository
import com.utsman.tokobola.network.ApiReducer

class ExploreUseCase(private val repository: ExploreRepository) {

    val brandReducer = ApiReducer<List<Brand>>()
    val categoryReducer = ApiReducer<List<Category>>()

    val productBrandReducer = ApiReducer<List<ThumbnailProduct>>()
    val productCategoryReducer = ApiReducer<List<ThumbnailProduct>>()

    val topProductReducer = ApiReducer<List<ThumbnailProduct>>()
    val curatedProductReducer = ApiReducer<List<ThumbnailProduct>>()

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

    suspend fun getProductBrand(brandId: Int) {
        // get page 1 on product
        productBrandReducer.transform(
            call = {
                repository.getProductBrand(brandId, 1)
            },
            mapper = { productResponse ->
                val dataResponse = productResponse.data?.data.orEmpty()
                dataResponse.map {
                    it.toThumbnailProduct()
                }
            }
        )
    }

    suspend fun getProductCategory(categoryId: Int) {
        // get page 1 on product
        productCategoryReducer.transform(
            call = {
                repository.getProductCategory(categoryId, 1)
            },
            mapper = { productResponse ->
                val dataResponse = productResponse.data?.data.orEmpty()
                dataResponse.map {
                    it.toThumbnailProduct()
                }
            }
        )
    }

    suspend fun getTopProduct() {
        topProductReducer.transform(
            call = {
                repository.getProductTop()
            },
            mapper = { response ->
                val data = response.data
                data?.map { it.toThumbnailProduct() }.orEmpty()
            }
        )
    }

    suspend fun getCuratedProduct() {
        curatedProductReducer.transform(
            call = {
                repository.getProductCurated()
            },
            mapper = { response ->
                val data = response.data
                data?.map { it.toThumbnailProduct() }.orEmpty()
            }
        )
    }

    fun clearData() {
        brandReducer.clear()
        categoryReducer.clear()
    }
}