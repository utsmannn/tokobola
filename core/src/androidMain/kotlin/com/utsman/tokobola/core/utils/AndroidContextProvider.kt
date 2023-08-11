package com.utsman.tokobola.core.utils

import android.content.Context

interface AndroidContextProvider {

    val context: Context

    companion object {
        @Volatile
        private var _contextProvider: AndroidContextProvider? = null

        fun setInstance(contextProvider: AndroidContextProvider) {
            _contextProvider = contextProvider
        }

        fun getInstance(): AndroidContextProvider {
            return synchronized(this) { _contextProvider ?: throw IllegalStateException("Context not provided") }
        }
    }
}