package com.utsman.tokobola.network

import com.utsman.tokobola.core.State
import io.ktor.client.plugins.ClientRequestException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

interface StateTransformation<U, T> {
    suspend fun transform(call: suspend () -> U, mapper: (U) -> T): State<T>

    companion object {
        @Suppress("FunctionName")
        inline fun <reified U, T>DefaultResponseTransform(): StateTransformation<U, T> {
            val json = Json {
                ignoreUnknownKeys = true
            }

            return object : StateTransformation<U, T> {
                override suspend fun transform(call: suspend () -> U, mapper: (U) -> T): State<T> {
                    val resultEvent: State<T> = try {
                        val dataSuccess = call.invoke()
                        val jsonString = Json.encodeToString(dataSuccess)

                        val jsonData = json.parseToJsonElement(jsonString)
                            .jsonObject

                        val status = jsonData["status"].toString().toBooleanStrict()
                        val message = jsonData["message"].toString()

                        if (status) {
                            val dataResult = mapper.invoke(dataSuccess)
                            State.Success(dataResult)
                        } else {
                            State.Failure(Throwable(message))
                        }
                    } catch (e: ClientRequestException) {
                        e.printStackTrace()
                        State.Failure(e)
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                        State.Failure(e)
                    } catch (e: Exception) {
                        State.Failure(e)
                    }

                    return resultEvent
                }

            }
        }

        @Suppress("FunctionName")
        inline fun <reified U, T>SimpleTransform(): StateTransformation<U, T> {
            return object : StateTransformation<U, T> {
                override suspend fun transform(call: suspend () -> U, mapper: (U) -> T): State<T> {
                    val data = call.invoke()
                    return State.Success(mapper.invoke(data))
                }

            }
        }
    }
}