package com.utsman.tokobola.core.utils

import com.utsman.tokobola.core.State

fun <U, T, S> State<T>.merge(other: State<U>, mapper: (data1: T, data2: U) -> S): State<S> {
    return when (this) {
        is State.Failure -> when (other) {
            is State.Failure -> State.Failure(other.exception)
            is State.Idle -> State.Idle()
            is State.Loading, is State.Success -> State.Loading()
        }

        is State.Idle -> when (other) {
            is State.Failure -> State.Failure(other.exception)
            is State.Idle, is State.Success -> State.Idle()
            is State.Loading -> State.Loading()
        }

        is State.Loading -> when (other) {
            is State.Failure -> State.Failure(other.exception)
            is State.Idle -> State.Idle()
            is State.Loading, is State.Success -> State.Loading()
        }

        is State.Success -> when (other) {
            is State.Failure -> State.Failure(other.exception)
            is State.Idle -> State.Idle()
            is State.Loading -> State.Loading()
            is State.Success -> State.Success(mapper.invoke(this.data, other.data))
        }
    }
}

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