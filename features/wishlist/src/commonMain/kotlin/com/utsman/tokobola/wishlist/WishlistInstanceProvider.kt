package com.utsman.tokobola.wishlist

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.wishlist.domain.WishlistRepository
import com.utsman.tokobola.wishlist.domain.WishlistUseCase
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

object WishlistInstanceProvider {
    private fun getRepository(): WishlistRepository {
        return WishlistRepository.create { WishlistRepository() }
    }

    fun providedUseCase(): WishlistUseCase {
        return WishlistUseCase.create { WishlistUseCase(getRepository()) }
    }
}

val LocalWishlistUseCase = compositionLocalOf<WishlistUseCase> { error("Not provided") }