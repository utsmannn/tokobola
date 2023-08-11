package com.utsman.tokobola

import android.app.Application
import android.content.Context
import com.utsman.tokobola.core.utils.AndroidContextProvider

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val androidContextProvider = object : AndroidContextProvider {
            override val context: Context
                get() = this@MainApplication
        }

        AndroidContextProvider.setInstance(androidContextProvider)
    }
}