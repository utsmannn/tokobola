package com.utsman.tokobola.core

import kotlin.jvm.Volatile

open class SingletonCreator<T : Any> : SynchronizObject() {

    @Volatile
    var instance: T? = null

    fun create(creator: () -> T): T {
        synchroniz(this) {
            if (instance == null) {
                instance = creator()
            }
            return instance as T
        }
    }

    fun getInstanceOrThrow(): T = checkNotNull(instance)
}