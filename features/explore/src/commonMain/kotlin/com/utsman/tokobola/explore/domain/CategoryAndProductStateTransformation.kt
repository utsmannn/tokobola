package com.utsman.tokobola.explore.domain

import com.utsman.tokobola.api.response.CategoryResponse
import com.utsman.tokobola.api.response.ThumbnailProductResponse
import com.utsman.tokobola.core.State
import com.utsman.tokobola.explore.ui.CategoryData
import com.utsman.tokobola.network.StateTransformation
import com.utsman.tokobola.network.response.BasePagedResponse

class CategoryAndProductStateTransformation :
    StateTransformation<List<Pair<CategoryResponse, BasePagedResponse.DataResponse<ThumbnailProductResponse>>>, List<CategoryData>> {

    override suspend fun transform(
        call: suspend () -> List<Pair<CategoryResponse, BasePagedResponse.DataResponse<ThumbnailProductResponse>>>,
        mapper: (List<Pair<CategoryResponse, BasePagedResponse.DataResponse<ThumbnailProductResponse>>>) -> List<CategoryData>
    ): State<List<CategoryData>> {
        val data = call.invoke()
        return State.Success(mapper.invoke(data))
    }


}