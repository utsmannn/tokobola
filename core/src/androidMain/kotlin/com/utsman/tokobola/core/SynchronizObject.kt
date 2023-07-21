package com.utsman.tokobola.core

actual open class SynchronizObject

actual inline fun <T> synchroniz(lock: Any, block: () -> T): T {
    return synchronized(lock, block)
}