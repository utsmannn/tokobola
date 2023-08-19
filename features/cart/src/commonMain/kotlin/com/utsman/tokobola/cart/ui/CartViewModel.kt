package com.utsman.tokobola.cart.ui

import com.utsman.tokobola.cart.domain.CartUseCase
import com.utsman.tokobola.common.entity.Cart
import com.utsman.tokobola.core.ViewModel
import com.utsman.tokobola.location.LocationTrackerProvider
import dev.icerock.moko.geo.LatLng
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val useCase: CartUseCase) : ViewModel() {

    val cartState get() = useCase.cartReducer.dataFlow.asStateFlow()

    val cartUiConfig: MutableStateFlow<CartUiConfig> = MutableStateFlow(CartUiConfig())

    val shippingLocationState = useCase.locationShippingReducer.dataFlow

    fun listenCart() = viewModelScope.launch {
        useCase.getCart()
    }

    fun pushCart(list: List<Cart>) {
        cartUiConfig.value = CartUiConfig(list)
    }

    fun incrementCart(productId: Int) {
        val currentCart = cartUiConfig.value
        val newCart = updateQuantityInCart(currentCart.carts, productId) {
            it+1
        }

        cartUiConfig.value = currentCart.copy(carts = newCart, time = getTimeMillis())
    }

    fun decrementCart(productId: Int) {
        val currentCart = cartUiConfig.value
        val newCart = updateQuantityInCart(currentCart.carts, productId) {
            it-1
        }
        cartUiConfig.value = currentCart.copy(carts = newCart, time = getTimeMillis())
    }

    private fun updateQuantityInCart(cart: List<Cart>, productIdToUpdate: Int, operation: (Int) -> Int): List<Cart> {
        val newCart = cart.map {
            if (it.product.id == productIdToUpdate) {
                it.quantity = operation.invoke(it.quantity)
            }
            it
        }

        return newCart
    }

    fun getShippingLocation() = viewModelScope.launch {
        useCase.getShippingLocation()
    }

    override fun onCleared() {
        viewModelScope.launch {
            useCase.stopLocationPlace()
        }
        viewModelScope.launch {
            useCase.updateCart(cartUiConfig.value.carts)
        }
        super.onCleared()
    }

}