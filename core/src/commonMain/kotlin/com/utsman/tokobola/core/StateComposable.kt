package com.utsman.tokobola.core

import androidx.compose.runtime.Composable

@Composable
fun <T : Any> State<T>.onLoading(content: @Composable () -> Unit) {
    if (this is State.Loading) { content.invoke() }
}

@Composable
fun <T : Any> State<T>.onSuccess(content: @Composable (T) -> Unit) {
    if (this is State.Success) { content.invoke(this.data) }
}

@Composable
fun <T : Any> State<T>.onFailure(content: @Composable (Throwable) -> Unit) {
    if (this is State.Failure) { content.invoke(this.exception) }
}

@Composable
fun <T : Any> State<T>.onIdle(content: @Composable () -> Unit) {
    if (this is State.Idle) { content.invoke() }
}
