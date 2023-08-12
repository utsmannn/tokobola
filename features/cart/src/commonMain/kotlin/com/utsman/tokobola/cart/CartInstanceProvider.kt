package com.utsman.tokobola.cart

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.cart.domain.CartRepository
import com.utsman.tokobola.cart.domain.CartUseCase

object CartInstanceProvider {

    private fun getRepository(): CartRepository {
        return CartRepository.create { CartRepository() }
    }

    fun providedUseCase(): CartUseCase {
        return CartUseCase.create { CartUseCase(getRepository()) }
    }
}

val LocalCartUseCase = compositionLocalOf<CartUseCase> { error("Not provided") }