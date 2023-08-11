package com.utsman.tokobola.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.setupDefaultComponents
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

@Composable
actual fun rememberImageLoader(): ImageLoader {
    return remember {
        val memCacheSize = 32 * 1024 * 1024
        val diskCacheSize = 512 * 1024 * 1024

        val cacheDir = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory, NSUserDomainMask, true
        ).first().toString()

        val cachePath = ("$cacheDir/media").toPath()

        ImageLoader {
            interceptor {
                memoryCacheConfig { maxSizeBytes(memCacheSize) }
                diskCacheConfig {
                    directory(cachePath)
                    maxSizeBytes(diskCacheSize.toLong())
                }
            }

            components { setupDefaultComponents() }
        }
    }
}