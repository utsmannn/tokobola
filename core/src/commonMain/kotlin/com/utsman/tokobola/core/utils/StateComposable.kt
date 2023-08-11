package com.utsman.tokobola.core.utils

import androidx.compose.runtime.Composable
import com.utsman.tokobola.core.State

@Composable
fun <T : Any> State<T>.onLoadingComposed(content: @Composable () -> Unit) {
    if (this is State.Loading) { content.invoke() }
}

@Composable
fun <T : Any> State<T>.onSuccessComposed(content: @Composable (T) -> Unit) {
    if (this is State.Success) { content.invoke(this.data) }
}

@Composable
fun <T : Any> State<T>.onFailureComposed(content: @Composable (Throwable) -> Unit) {
    if (this is State.Failure) { content.invoke(this.exception) }
}