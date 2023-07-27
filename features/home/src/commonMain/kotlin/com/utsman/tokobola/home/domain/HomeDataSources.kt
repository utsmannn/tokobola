package com.utsman.tokobola.home.domain

import com.utsman.tokobola.common.entity.response.HomeBannerResponse
import com.utsman.tokobola.common.entity.response.ThumbnailProductResponse
import com.utsman.tokobola.network.NetworkSources
import com.utsman.tokobola.network.response.BasePagedResponse
import com.utsman.tokobola.network.response.BaseResponse

class HomeDataSources : NetworkSources("https://footballstore.fly.dev/api") {

    suspend fun getProductPaged(): BasePagedResponse<ThumbnailProductResponse> {
        return getPaged("/v2/product")
    }

    suspend fun getBanner(): BaseResponse<List<HomeBannerResponse>> {
        return get("/product/banner")
    }
}