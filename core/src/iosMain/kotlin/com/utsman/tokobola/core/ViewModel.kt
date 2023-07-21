package com.utsman.tokobola.core

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

actual abstract class ViewModel {
    actual val viewModelScope = MainScope()

    protected actual open fun onCleared() {
        viewModelScope.cancel()
    }

    actual fun clear() {
        onCleared()
    }
}