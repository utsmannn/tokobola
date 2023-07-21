package com.utsman.tokobola.core

import androidx.compose.runtime.Composable
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.setupDefaultComponents
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

internal actual fun ComponentRegistryBuilder.setupComponents(androidContext: Any?) {
    this.setupDefaultComponents()
}


internal actual fun getImageCacheDirectoryPath(androidContext: Any?): Path {
    val cacheDir = NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory, NSUserDomainMask, true
    ).first() as String

    return ("$cacheDir/media").toPath()
}

@Composable
actual fun appImageLoader(): ImageLoader {
    val memCacheSize = 32 * 1024 * 1024
    val diskCacheSize = 512 * 1024 * 1024

    return ImageLoader {
        interceptor {
            memoryCacheConfig { maxSizeBytes(memCacheSize) }
            diskCacheConfig {
                directory(getImageCacheDirectoryPath())
                maxSizeBytes(diskCacheSize.toLong())
            }
        }

        components { setupComponents() }
    }
}