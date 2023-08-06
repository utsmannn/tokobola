package com.utsman.tokobola.network

import com.utsman.tokobola.core.State
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

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

    suspend inline fun <reified U> transformFlow(
        transformation: StateFlowTransformation<U, T> = StateFlowTransformation.DefaultFlowTransform<U, T>(),
        noinline call: suspend () -> Flow<U>,
        noinline mapper: (U) -> T
    ) {
        dataFlow.value = State.Loading()
        val result = transformation.transform(call, mapper)
        dataFlow.value = result
    }

    fun clear() {
        dataFlow.value = State.Idle()
    }
}