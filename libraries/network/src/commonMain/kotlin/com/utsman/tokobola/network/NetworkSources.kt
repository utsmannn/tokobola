package com.utsman.tokobola.network

import com.utsman.tokobola.network.response.BasePagedResponse
import com.utsman.tokobola.network.response.BaseResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

abstract class NetworkSources(protected val baseUrl: String) {

    protected fun client() = ClientProvider.client()

    protected suspend inline fun <reified T> get(
        endPoint: String,
        contentType: ContentType = ContentType.Application.Json
    ): BaseResponse<T> {
        return client().get("$baseUrl$endPoint") {
            contentType(contentType)
        }.body()
    }

    protected suspend inline fun <reified T> getPaged(
        endPoint: String,
        contentType: ContentType = ContentType.Application.Json
    ): BasePagedResponse<T> {
        return client().get("$baseUrl$endPoint") {
            contentType(contentType)
        }.body()
    }
}