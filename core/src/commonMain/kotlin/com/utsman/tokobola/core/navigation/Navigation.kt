package com.utsman.tokobola.core.navigation

import androidx.compose.runtime.compositionLocalOf

interface Navigation {

    fun goToDetail(id: Int): Boolean
}

val LocalNavigation = compositionLocalOf<Navigation> { error("navigation failure") }