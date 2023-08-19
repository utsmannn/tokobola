package com.utsman.tokobola.cart.ui

import com.utsman.tokobola.cart.domain.LocationPickerUseCase
import com.utsman.tokobola.core.ViewModel
import kotlinx.coroutines.launch

class LocationPickerViewModel(private val useCase: LocationPickerUseCase) : ViewModel() {

    val query get() = useCase.query
    val locationState get() = useCase.locationState

    fun getLocation() = viewModelScope.launch {
        useCase.getLocation()
    }

    override fun onCleared() {
        viewModelScope.launch {
            useCase.stopLocation()
        }
        super.onCleared()
    }
}