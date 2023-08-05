package com.utsman.tokobola.explore.ui

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.ThumbnailProduct

data class BrandData(
    val brand: Brand,
    val products: List<ThumbnailProduct> = emptyList()
)