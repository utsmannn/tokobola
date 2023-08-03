package com.utsman.tokobola.explore.domain

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.common.toBrand
import com.utsman.tokobola.common.toCategory
import com.utsman.tokobola.common.toHomeProduct
import com.utsman.tokobola.core.data.orNol
import com.utsman.tokobola.core.utils.pmap
import com.utsman.tokobola.explore.ui.CategoryData
import com.utsman.tokobola.network.ApiReducer
import com.utsman.tokobola.network.response.BasePagedResponse

class ExploreUseCase(private val repository: ExploreRepository) {

    val brandReducer = ApiReducer<List<Brand>>()
    val categoryReducer = ApiReducer<List<Category>>()
    val productCategory = ApiReducer<List<ThumbnailProduct>>()

    val categoryAndProductReducer = ApiReducer<List<CategoryData>>()

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

    suspend fun getAllCategoryAndProduct() {
        categoryAndProductReducer.transform(
            transformation = CategoryAndProductStateTransformation(),
            call = {
                val categoryResponse = repository.getCategory()
                categoryResponse.data
                    ?.filter { it.id != 40 }
                    ?.pmap {
                        val productPaging = repository.getProductCategory(it.id.orNol(), 1)
                        Pair(it, productPaging.data ?: BasePagedResponse.DataResponse())
                    }.orEmpty()
            },
            mapper = { data ->
                data.map { (categoryResponse, dataResponse) ->
                    val category = categoryResponse.toCategory()
                    val product = dataResponse.data.map { it.toHomeProduct() }
                    CategoryData(category, product)
                }
            }
        )
    }

    fun clearData() {
        brandReducer.clear()
        categoryReducer.clear()
    }
}