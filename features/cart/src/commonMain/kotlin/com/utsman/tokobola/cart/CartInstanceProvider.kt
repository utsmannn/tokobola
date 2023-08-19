package com.utsman.tokobola.cart

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.cart.domain.CartRepository
import com.utsman.tokobola.cart.domain.CartUseCase
import com.utsman.tokobola.cart.domain.LocationPickerUseCase
import com.utsman.tokobola.location.LocationTrackerProvider

object CartInstanceProvider {

    private fun getRepository(): CartRepository {
        return CartRepository.create { CartRepository() }
    }

    fun providedCartUseCase(locationTrackerProvider: LocationTrackerProvider): CartUseCase {
        return CartUseCase.create { CartUseCase(getRepository(), locationTrackerProvider) }
    }

    fun providedLocationPickerUseCase(): LocationPickerUseCase {
        return LocationPickerUseCase.create { LocationPickerUseCase(getRepository()) }
    }
}

val LocalCartUseCase = compositionLocalOf<CartUseCase> { error("Not provided") }
val LocalLocationPickerUseCase = compositionLocalOf<LocationPickerUseCase> { error("Not provided") }