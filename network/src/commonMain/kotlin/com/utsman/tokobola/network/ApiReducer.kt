package com.utsman.tokobola.network

import com.utsman.tokobola.core.State
import com.utsman.tokobola.core.data.orFalse
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

@Serializable
data class ApiResponse(
    val data: String,
    val message: String?,
    @SerialName("status")
    val status: Boolean
)

open class ApiReducer<T> {

    val dataFlow: MutableStateFlow<State<T>> = MutableStateFlow(State.Idle())

    inline fun <reified U> transform(call: () -> U, mapper: (U) -> T) {
        val json = Json {
            ignoreUnknownKeys = true
        }

        dataFlow.value = State.Loading()
        val resultEvent: State<T> = try {
            val dataSuccess = call.invoke()
            val jsonString = Json.encodeToString(dataSuccess)

            val jsonData = json.parseToJsonElement(jsonString)
                .jsonObject

            val status = jsonData["status"].toString().toBooleanStrict()
            val message = jsonData["message"].toString()
            val data = jsonData["data"]
            val isPaging = data?.toString()?.contains("\"has_next_page\":").orFalse()

            if (status) {
                val dataResult = mapper.invoke(dataSuccess)
                State.Success(dataResult)
            } else {
                State.Failure(Throwable(message))
            }
        } catch (e: ClientRequestException) {
            e.printStackTrace()
            State.Failure(e)
        } catch (e: Exception) {
            e.printStackTrace()
            State.Failure(e)
        }
        dataFlow.value = resultEvent
    }

    fun clear() {
        dataFlow.value = State.Idle()
    }
}