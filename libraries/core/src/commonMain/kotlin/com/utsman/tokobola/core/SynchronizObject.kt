package com.utsman.tokobola.core

expect open class SynchronizObject()

expect inline fun <T> synchroniz(lock: Any, block: () -> T): T