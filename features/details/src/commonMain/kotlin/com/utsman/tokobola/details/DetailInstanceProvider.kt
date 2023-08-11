package com.utsman.tokobola.details

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.details.domain.BrandDetailUseCase
import com.utsman.tokobola.details.domain.CategoryDetailUseCase
import com.utsman.tokobola.details.domain.DetailRepository
import com.utsman.tokobola.details.domain.ProductDetailUseCase
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object DetailInstanceProvider : SynchronizObject() {

    @Volatile
    private var repository: DetailRepository? = null
    @Volatile
    private var productDetailUseCase: ProductDetailUseCase? = null
    @Volatile
    private var categoryDetailUseCase: CategoryDetailUseCase? = null
    @Volatile
    private var brandDetailUseCase: BrandDetailUseCase? = null

    private fun getRepository(): DetailRepository {
        if (repository == null) repository = DetailRepository()
        return synchroniz(this) { repository!! }
    }

    fun providedProductDetailUseCase(): ProductDetailUseCase {
        if (productDetailUseCase == null) productDetailUseCase = ProductDetailUseCase(getRepository())
        return synchroniz(this) { productDetailUseCase!! }
    }

    fun providedCategoryDetailUseCase(): CategoryDetailUseCase {
        if (categoryDetailUseCase == null) categoryDetailUseCase = CategoryDetailUseCase(getRepository())
        return synchroniz(this) { categoryDetailUseCase!! }
    }

    fun providedBrandDetailUseCase(): BrandDetailUseCase {
        if (brandDetailUseCase == null) brandDetailUseCase = BrandDetailUseCase(getRepository())
        return synchroniz(this) { brandDetailUseCase!! }
    }
}

val LocalProductDetailUseCase = compositionLocalOf<ProductDetailUseCase> { error("Not provided") }
val LocalCategoryDetailUseCase = compositionLocalOf<CategoryDetailUseCase> { error("Not provided") }
val LocalBrandDetailUseCase = compositionLocalOf<BrandDetailUseCase> { error("Not provided") }