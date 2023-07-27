package com.utsman.tokobola.common

import com.utsman.tokobola.common.entity.response.HomeBannerResponse
import com.utsman.tokobola.common.entity.response.ProductResponse
import com.utsman.tokobola.common.entity.response.ThumbnailProductResponse
import com.utsman.tokobola.common.entity.ui.HomeBanner
import com.utsman.tokobola.common.entity.ui.Product
import com.utsman.tokobola.common.entity.ui.ThumbnailProduct
import com.utsman.tokobola.core.data.orFalse
import com.utsman.tokobola.core.data.orNol

fun ProductResponse.mapToProduct(): Product {
    return Product(
        id = this.id.orNol(),
        name = this.name.orEmpty(),
        category = this.category?.name.orEmpty(),
        images = this.images?.filterNotNull().orEmpty(),
        description = this.description.orEmpty(),
        price = this.price.orNol(),
        isPromoted = this.promoted.orFalse(),
        brand = this.brand?.name.orEmpty()
    )
}

fun ThumbnailProductResponse.toHomeProduct(): ThumbnailProduct {
    return ThumbnailProduct(
        id = id.orNol(),
        name = name.orEmpty(),
        price = price.orNol(),
        category = ThumbnailProduct.ThumbnailCategory(
            id = category?.id.orNol(),
            name = category?.name.orEmpty()
        ),
        brand = ThumbnailProduct.ThumbnailBrand(
            id = brand?.id.orNol(),
            name = brand?.name.orEmpty(),
            image = brand?.image.orEmpty()
        ),
        image = image.orEmpty(),
        promoted = promoted.orFalse()
    )
}

fun HomeBannerResponse.toHomeBanner(): HomeBanner {
    return HomeBanner(
        id = id.orNol(),
        productId = productId.orNol(),
        colorPrimary = colorPrimary ?: "#FFFFFF",
        colorAccent = colorAccent ?: "#000000",
        productImage = productImage.orEmpty(),
        description = description.orEmpty()
    )
}