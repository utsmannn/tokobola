package com.utsman.tokobola.core

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.setupDefaultComponents
import okio.Path
import okio.Path.Companion.toPath

internal actual fun ComponentRegistryBuilder.setupComponents(androidContext: Any?) {
    setupDefaultComponents(androidContext as Context)
}

internal actual fun getImageCacheDirectoryPath(androidContext: Any?): Path {
    return (androidContext as Context).cacheDir.absolutePath.toPath()
}

@Composable
actual fun appImageLoader(): ImageLoader {
    val context = LocalContext.current
    val memCacheSize = 32 * 1024 * 1024
    val diskCacheSize = 512 * 1024 * 1024

    return ImageLoader {
        interceptor {
            memoryCacheConfig { maxSizeBytes(memCacheSize) }
            diskCacheConfig {
                directory(getImageCacheDirectoryPath(context))
                maxSizeBytes(diskCacheSize.toLong())
            }
        }

        components {
            setupComponents(context)
        }
    }
}