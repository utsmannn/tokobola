package com.utsman.tokobola.details.entity

import com.utsman.tokobola.core.orFalse
import com.utsman.tokobola.core.orNol
import com.utsman.tokobola.details.domain.ProductDetailResponse

fun ProductDetailResponse.mapToProduct(): ProductDetail {
    return ProductDetail(
        id = this.id.orNol(),
        name = this.name.orEmpty(),
        category = this.category.orEmpty(),
        image = this.image?.filterNotNull().orEmpty(),
        description = this.description.orEmpty(),
        price = this.price.orNol(),
        promoted = this.promoted.orFalse()
    )
}