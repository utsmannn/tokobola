package com.utsman.tokobola.home.entity

import com.utsman.tokobola.core.data.orFalse
import com.utsman.tokobola.core.data.orNol
import com.utsman.tokobola.home.domain.ProductResponse

fun ProductResponse.mapToProduct(): Product {
    return Product(
        id = this.id.orNol(),
        name = this.name.orEmpty(),
        category = this.category?.name.orEmpty(),
        image = this.image?.filterNotNull().orEmpty(),
        description = this.description.orEmpty(),
        price = this.price.orNol(),
        isPromoted = this.promoted.orFalse(),
        brand = this.brand?.name.orEmpty()
    )
}