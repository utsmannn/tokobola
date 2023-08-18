package com.utsman.tokobola.core

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.setupDefaultComponents
import com.utsman.tokobola.core.utils.AndroidContextProvider
import okio.Path
import okio.Path.Companion.toPath

private fun getImageCacheDirectoryPath(context: Context): Path {
    return context.cacheDir.absolutePath.toPath()
}

@Composable
actual fun rememberImageLoader(): ImageLoader {
    return remember {
        val context = AndroidContextProvider.getInstance().context
        val memCacheSize = 32 * 1024 * 1024
        val diskCacheSize = 512 * 1024 * 1024

        ImageLoader {
            interceptor {
                memoryCacheConfig { maxSizeBytes(memCacheSize) }
                diskCacheConfig {
                    directory(getImageCacheDirectoryPath(context))
                    maxSizeBytes(diskCacheSize.toLong())
                }
            }

            components {
                setupDefaultComponents(context)
            }
        }
    }
}