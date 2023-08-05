package com.utsman.tokobola.common

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.HomeBanner
import com.utsman.tokobola.common.entity.Product
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.core.data.orFalse
import com.utsman.tokobola.core.data.orNol
import com.utsman.tokobola.api.response.BrandResponse
import com.utsman.tokobola.api.response.CategoryResponse
import com.utsman.tokobola.api.response.HomeBannerResponse
import com.utsman.tokobola.api.response.ProductResponse
import com.utsman.tokobola.api.response.ThumbnailProductResponse
import com.utsman.tokobola.common.entity.Category

fun ProductResponse.mapToProduct(): Product {
    return Product(
        id = this.id.orNol(),
        name = this.name.orEmpty(),
        category = this.category?.name.orEmpty(),
        images = this.images?.filterNotNull().orEmpty(),
        description = this.description.orEmpty(),
        price = this.price.orNol(),
        isPromoted = this.promoted.orFalse(),
        brand = ThumbnailProduct.ThumbnailBrand(
            id = brand?.id.orNol(),
            name = brand?.name.orEmpty(),
            logo = brand?.logo.orEmpty()
        )
    )
}

fun ThumbnailProductResponse.toThumbnailProduct(): ThumbnailProduct {
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
            logo = brand?.logo.orEmpty()
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

fun BrandResponse.toBrand(): Brand {
    return Brand(
        id = id.orNol(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        image = image.orEmpty(),
        logo = logo.orEmpty()
    )
}

fun CategoryResponse.toCategory(): Category {
    return Category(
        id = id.orNol(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        image = image.orEmpty()
    )
}