package com.utsman.tokobola.api

import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.network.response.BasePagedResponse
import com.utsman.tokobola.network.response.BaseResponse
import com.utsman.tokobola.api.response.BrandResponse
import com.utsman.tokobola.api.response.CategoryResponse
import com.utsman.tokobola.api.response.HomeBannerResponse
import com.utsman.tokobola.api.response.ProductResponse
import com.utsman.tokobola.api.response.ThumbnailProductResponse
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

class ProductWebApi : WebDataSource() {

    suspend fun getByFeaturedPaged(page: Int): BasePagedResponse<ThumbnailProductResponse> {
        return getPaged(WebEndPoint.PRODUCT_FEATURED.withParam("page", page))
    }

    suspend fun getByBrandPaged(brandId: Int, page: Int): BasePagedResponse<ThumbnailProductResponse> {
        return getPaged(WebEndPoint.PRODUCT_BRAND.withParam("page", page).withParam("brand_id", brandId))
    }

    suspend fun getByCategoryPaged(categoryId: Int, page: Int): BasePagedResponse<ThumbnailProductResponse> {
        return getPaged(WebEndPoint.PRODUCT_CATEGORY.withParam("page", page).withParam("category_id", categoryId))
    }

    suspend fun getTop(): BaseResponse<List<ThumbnailProductResponse>> {
        return get(WebEndPoint.PRODUCT_TOP)
    }

    suspend fun getCurated(): BaseResponse<List<ThumbnailProductResponse>> {
        return get(WebEndPoint.PRODUCT_CURATED)
    }

    suspend fun getDetail(productId: Int): BaseResponse<ProductResponse> {
        return get(WebEndPoint.PRODUCT_DETAIL.withParam("product_id", productId))
    }

    suspend fun getHomeBanner(): BaseResponse<List<HomeBannerResponse>> {
        return get(WebEndPoint.BANNER)
    }

    suspend fun getBrand(): BaseResponse<List<BrandResponse>> {
        return get(WebEndPoint.BRAND)
    }

    suspend fun getCategory() : BaseResponse<List<CategoryResponse>> {
        return get(WebEndPoint.CATEGORY)
    }

    @ThreadLocal
    companion object : SynchronizObject() {

        @Volatile
        private var webApi: ProductWebApi? = null

        fun getInstance(): ProductWebApi {
            if (webApi == null) webApi = ProductWebApi()
            return synchroniz(this) { webApi ?: ProductWebApi() }
        }
    }
}