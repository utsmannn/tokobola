package com.utsman.tokobola.network

import com.utsman.tokobola.core.State
import kotlinx.coroutines.flow.MutableStateFlow

open class ApiReducer<T> {

    val dataFlow: MutableStateFlow<State<T>> = MutableStateFlow(State.Idle())

    suspend inline fun <reified U> transform(
        transformation: StateTransformation<U, T> = StateTransformation.DefaultResponseTransform<U, T>(),
        noinline call: suspend () -> U,
        noinline mapper: (U) -> T
    ) {
        dataFlow.value = State.Loading()
        val result = transformation.transform(call, mapper)
        dataFlow.value = result
    }

    fun forcePushState(state: State<T>) {
        dataFlow.value = state
    }

    fun clear() {
        dataFlow.value = State.Idle()
    }
}