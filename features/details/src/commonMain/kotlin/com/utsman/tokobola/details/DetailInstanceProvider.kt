package com.utsman.tokobola.details

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.details.domain.BrandDetailUseCase
import com.utsman.tokobola.details.domain.CategoryDetailUseCase
import com.utsman.tokobola.details.domain.DetailRepository
import com.utsman.tokobola.details.domain.ProductDetailUseCase

object DetailInstanceProvider {

    private fun getRepository(): DetailRepository {
        return DetailRepository.create { DetailRepository() }
    }

    fun providedProductDetailUseCase(): ProductDetailUseCase {
        return ProductDetailUseCase.create { ProductDetailUseCase(getRepository()) }
    }

    fun providedCategoryDetailUseCase(): CategoryDetailUseCase {
        return CategoryDetailUseCase.create { CategoryDetailUseCase(getRepository()) }
    }

    fun providedBrandDetailUseCase(): BrandDetailUseCase {
        return BrandDetailUseCase.create { BrandDetailUseCase(getRepository()) }
    }
}

val LocalProductDetailUseCase = compositionLocalOf<ProductDetailUseCase> { error("Not provided") }
val LocalCategoryDetailUseCase = compositionLocalOf<CategoryDetailUseCase> { error("Not provided") }
val LocalBrandDetailUseCase = compositionLocalOf<BrandDetailUseCase> { error("Not provided") }