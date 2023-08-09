package com.utsman.tokobola.database

import kotlinx.coroutines.flow.MutableStateFlow

class LocalReducer<T> {
    val dataFlow: MutableStateFlow<LocalState<T>> = MutableStateFlow(LocalState.Empty())

    suspend inline fun <reified U> transform(
        noinline sources: suspend () -> U?,
        noinline mapper: (U) -> T
    ) {

        val data = sources.invoke()
        if (data == null) {
            dataFlow.value = LocalState.Empty()
            return
        }

        val result = mapper.invoke(data)
        dataFlow.value = LocalState.Found(result)
    }
}