package com.utsman.tokocot.api

import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.network.response.BasePagedResponse
import com.utsman.tokobola.network.response.BaseResponse
import com.utsman.tokocot.api.response.BrandResponse
import com.utsman.tokocot.api.response.HomeBannerResponse
import com.utsman.tokocot.api.response.ProductResponse
import com.utsman.tokocot.api.response.ThumbnailProductResponse
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

class ProductWebApi : WebDataSource() {

    suspend fun getFeaturedPaged(page: Int): BasePagedResponse<ThumbnailProductResponse> {
        return getPaged(WebEndPoint.PRODUCT_FEATURED.withParam("page", page))
    }

    suspend fun getByBrandPaged(brandId: Int, page: Int): BasePagedResponse<ThumbnailProductResponse> {
        return getPaged(WebEndPoint.PRODUCT_BRAND.withParam("page", page).withParam("brand_id", brandId))
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