package com.utsman.tokobola.core.utils

import android.content.Context
import com.utsman.tokobola.core.SingletonCreator

interface AndroidContextProvider {

    val context: Context

    companion object : SingletonCreator<AndroidContextProvider>() {
        fun getInstance(): AndroidContextProvider {
            return synchronized(this) { instance as AndroidContextProvider }
        }

    }
}