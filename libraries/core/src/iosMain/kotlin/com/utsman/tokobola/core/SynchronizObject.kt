package com.utsman.tokobola.core

import kotlinx.atomicfu.locks.synchronized
import kotlinx.atomicfu.locks.SynchronizedObject as AtomicSynchronizedObject

actual open class SynchronizObject : AtomicSynchronizedObject()

actual inline fun <T> synchroniz(lock: Any, block: () -> T): T {
    return if (lock is AtomicSynchronizedObject) {
        synchronized(lock, block)
    } else {
        throw IllegalArgumentException("Lock param invalid!")
    }
}
