package com.utsman.tokobola.explore.domain

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.common.toBrand
import com.utsman.tokobola.common.toCategory
import com.utsman.tokobola.network.ApiReducer

class ExploreUseCase(private val repository: ExploreRepository) {

    val brandReducer = ApiReducer<List<Brand>>()
    val categoryReducer = ApiReducer<List<Category>>()

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

    fun clearData() {
        brandReducer.clear()
        categoryReducer.clear()
    }
}