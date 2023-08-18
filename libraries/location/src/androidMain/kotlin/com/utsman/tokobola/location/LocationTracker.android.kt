package com.utsman.tokobola.location

import com.utsman.tokobola.core.utils.AndroidContextProvider
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController

internal actual val locationTracker: LocationTracker
    get() {
        val contextProvider = AndroidContextProvider.getInstance()
        return LocationTracker(
            PermissionsController(applicationContext = contextProvider.context)
        )
    }