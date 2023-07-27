package com.utsman.tokobola.network

import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.native.concurrent.ThreadLocal


@ThreadLocal
internal object ClientProvider : SynchronizObject() {

    private var _client: HttpClient? = null

    fun client(): HttpClient {
        if (_client == null) {
            _client = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
            }
        }
        return synchroniz(this) { _client!! }
    }
}