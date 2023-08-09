package com.utsman.tokobola.database

sealed class LocalState<T> {
    data class Found<T>(val data: T): LocalState<T>()
    class Empty<T> : LocalState<T>()
}