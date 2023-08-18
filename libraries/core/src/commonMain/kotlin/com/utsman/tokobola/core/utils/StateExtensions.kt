package com.utsman.tokobola.core.utils

import com.utsman.tokobola.core.State

fun <T : Any> State<T>.onLoading(content: () -> Unit) {
    if (this is State.Loading) {
        content.invoke()
    }
}

fun <T : Any> State<T>.onSuccess(content: (T) -> Unit) {
    if (this is State.Success) {
        content.invoke(this.data)
    }
}

fun <T : Any> State<T>.onFailure(content: (Throwable) -> Unit) {
    if (this is State.Failure) {
        content.invoke(this.exception)
    }
}

fun <T : Any> State<T>.onIdle(content: () -> Unit) {
    if (this is State.Idle) {
        content.invoke()
    }
}

fun <T> State<T>.getOrNull(): T? {
    return when (this) {
        is State.Success -> {
            data
        }

        else -> {
            null
        }
    }
}