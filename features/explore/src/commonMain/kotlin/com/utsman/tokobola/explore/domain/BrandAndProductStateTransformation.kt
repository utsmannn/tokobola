package com.utsman.tokobola.explore.domain

import com.utsman.tokobola.api.response.BrandResponse
import com.utsman.tokobola.api.response.ThumbnailProductResponse
import com.utsman.tokobola.core.State
import com.utsman.tokobola.explore.ui.BrandData
import com.utsman.tokobola.network.StateTransformation
import com.utsman.tokobola.network.response.BasePagedResponse

class BrandAndProductStateTransformation : StateTransformation<List<Pair<BrandResponse, BasePagedResponse.DataResponse<ThumbnailProductResponse>>>, List<BrandData>> {
    override suspend fun transform(
        call: suspend () -> List<Pair<BrandResponse, BasePagedResponse.DataResponse<ThumbnailProductResponse>>>,
        mapper: (List<Pair<BrandResponse, BasePagedResponse.DataResponse<ThumbnailProductResponse>>>) -> List<BrandData>
    ): State<List<BrandData>> {
        val data = call.invoke()
        return State.Success(mapper.invoke(data))
    }
}