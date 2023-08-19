package com.utsman.tokobola.cart

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.cart.domain.CartRepository
import com.utsman.tokobola.cart.domain.CartUseCase
import com.utsman.tokobola.location.LocationTrackerProvider

object CartInstanceProvider {

    private fun getRepository(): CartRepository {
        return CartRepository.create { CartRepository() }
    }

    fun providedUseCase(locationTrackerProvider: LocationTrackerProvider): CartUseCase {
        return CartUseCase.create { CartUseCase(getRepository(), locationTrackerProvider) }
    }
}

val LocalCartUseCase = compositionLocalOf<CartUseCase> { error("Not provided") }