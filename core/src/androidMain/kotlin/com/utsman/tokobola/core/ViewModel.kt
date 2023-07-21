package com.utsman.tokobola.core

import androidx.lifecycle.ViewModel as LifecycleViewModel
import androidx.lifecycle.viewModelScope as androidViewModelScope

actual abstract class ViewModel : LifecycleViewModel() {
    actual val viewModelScope = androidViewModelScope

    actual override fun onCleared() {
        super.onCleared()
    }

    actual fun clear() {
        onCleared()
    }
}