package com.utsman.tokobola.cart.ui

import com.utsman.tokobola.cart.domain.LocationPickerUseCase
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.core.data.LatLon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class LocationPickerViewModel(private val useCase: LocationPickerUseCase) : ViewModel() {

    val query get() = useCase.query
    val locationResultState = useCase.locationSearchReducer.dataFlow
    val locationReverseState = useCase.locationReverseReducer.dataFlow

    private var proximityLatLon: LatLon? = null

    fun listenQuery() = viewModelScope.launch {
        query.debounce(2000)
            .distinctUntilChanged()
            .collectLatest {
                proximityLatLon?.let { latLon ->
                    useCase.searchLocationPlace(it, latLon)
                }
            }
    }

    fun updateProximityLatLon(latLon: LatLon) {
        proximityLatLon = latLon
    }

    fun getLocationReverse(latLon: LatLon) = viewModelScope.launch {
        useCase.getLocationReverse(latLon)
    }

    fun clearData() {
        useCase.clearData()
    }

    override fun onCleared() {
        clearData()
        super.onCleared()
    }
}