package com.utsman.tokobola.api

import com.utsman.tokobola.network.response.BasePagedResponse
import com.utsman.tokobola.network.response.BaseResponse
import com.utsman.tokobola.api.response.BrandResponse
import com.utsman.tokobola.api.response.CategoryResponse
import com.utsman.tokobola.api.response.HomeBannerResponse
import com.utsman.tokobola.api.response.ProductResponse
import com.utsman.tokobola.api.response.ThumbnailProductResponse
import com.utsman.tokobola.core.SingletonCreator

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

    suspend fun getBySearch(page: Int, query: String): BasePagedResponse<ThumbnailProductResponse> {
        return getPaged(WebEndPoint.PRODUCT_SEARCH.withParam("page", page).withParam("query", query))
    }

    suspend fun getThumbnailByIds(ids: List<Int>): BaseResponse<List<ThumbnailProductResponse>> {
        val newIds = ids.toString().replace("[", "").replace("]", "")
            .replace(" ", "")
        return get(WebEndPoint.PRODUCT_THUMBNAIL.withParam("id", newIds))
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

    companion object : SingletonCreator<ProductWebApi>()
}