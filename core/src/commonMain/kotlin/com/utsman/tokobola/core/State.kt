package com.utsman.tokobola.core

sealed class State<T> {
    class Idle<T> : State<T>()
    class Loading<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Failure<T>(val exception: Throwable) : State<T>()

    override fun toString(): String {
        return "${this::class.simpleName}"
    }
}