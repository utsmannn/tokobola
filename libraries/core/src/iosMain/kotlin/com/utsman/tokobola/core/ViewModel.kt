package com.utsman.tokobola.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

actual abstract class ViewModel {
    actual val viewModelScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.IO
    }

    protected actual open fun onCleared() {
        viewModelScope.cancel()
    }

    actual fun clear() {
        onCleared()
    }
}