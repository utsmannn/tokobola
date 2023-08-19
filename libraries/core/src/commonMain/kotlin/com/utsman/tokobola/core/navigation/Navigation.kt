package com.utsman.tokobola.core.navigation

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.data.LatLon

interface Navigation {
    fun back(): Boolean

    fun goToDetailProduct(id: Int): Boolean
    fun goToDetailCategory(categoryId: Int): Boolean
    fun goToDetailBrand(brandId: Int): Boolean
    fun goToSearch(): Boolean
    fun goToCart(): Boolean
    fun goToLocationPicker(latLon: LatLon): Boolean
}

val LocalNavigation = compositionLocalOf<Navigation> { error("navigation failure") }