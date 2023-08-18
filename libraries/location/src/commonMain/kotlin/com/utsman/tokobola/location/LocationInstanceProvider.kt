package com.utsman.tokobola.location

import androidx.compose.runtime.compositionLocalOf

object LocationInstanceProvider {

    fun providedLocationTrackerProvider(): LocationTrackerProvider {
        return LocationTrackerProvider.create { LocationTrackerProvider() }
    }
}

val LocalLocationTrackerProvider = compositionLocalOf<LocationTrackerProvider> { error("Not provider") }