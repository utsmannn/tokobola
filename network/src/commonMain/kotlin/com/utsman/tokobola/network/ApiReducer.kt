package com.utsman.tokobola.network

import com.utsman.tokobola.core.State
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ApiReducer<T> {
    private val _dataFlow: MutableStateFlow<State<T>> = MutableStateFlow(State.Idle())
    val dataFlow: StateFlow<State<T>> get() = _dataFlow

    suspend fun <U> transform(call: suspend () -> U, mapper: (U) -> T) {
        _dataFlow.value = State.Loading()
        val resultEvent: State<T> = try {
            val dataSuccess = call.invoke()
            val dataResult = mapper.invoke(dataSuccess)
            State.Success(dataResult)
        } catch (e: ClientRequestException) {
            e.printStackTrace()
            State.Failure(e)
        } catch (e: Exception) {
            e.printStackTrace()
            State.Failure(e)
        }
        _dataFlow.value = resultEvent
    }

    suspend fun clear() {
        _dataFlow.value = State.Idle()
    }
}