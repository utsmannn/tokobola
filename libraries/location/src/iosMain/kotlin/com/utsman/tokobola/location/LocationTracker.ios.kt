package com.utsman.tokobola.location

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.ios.PermissionsController
import platform.CoreLocation.kCLLocationAccuracyBest

internal actual val locationTracker: LocationTracker
    get() {
        return LocationTracker(
            permissionsController = PermissionsController(),
            accuracy = kCLLocationAccuracyBest
        )
    }