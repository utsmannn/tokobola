package com.utsman.tokobola.explore.ui

import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.common.entity.ThumbnailProduct

data class CategoryData(
    val category: Category = Category(),
    val products: List<ThumbnailProduct> = emptyList()
)