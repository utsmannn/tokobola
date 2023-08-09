package com.utsman.tokobola.core.navigation

import androidx.compose.runtime.compositionLocalOf

interface Navigation {
    fun back(): Boolean

    fun goToDetail(id: Int): Boolean

    fun goToSearch(): Boolean
}

val LocalNavigation = compositionLocalOf<Navigation> { error("navigation failure") }