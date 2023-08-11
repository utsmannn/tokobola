package com.utsman.tokobola.explore.ui

import com.utsman.tokobola.common.entity.Brand
import com.utsman.tokobola.common.entity.Category
import com.utsman.tokobola.common.entity.ThumbnailProduct
import com.utsman.tokobola.core.State

data class ExploreUiConfig(
    val offsetTabCategory: Float = 0f,
    val heightTabCategory: Int = 0,
    val selectedTabCategoryIndex: Int = 0,
    val selectedTabBrandIndex: Int = 0,
    val selectedCategory: Category = Category(id = 1),
    val selectedBrand: Brand = Brand(id = 1),
    val selectedProductCategory: State<List<ThumbnailProduct>> = State.Loading()
)