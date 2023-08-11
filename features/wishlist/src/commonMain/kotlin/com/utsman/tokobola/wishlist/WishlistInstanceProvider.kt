package com.utsman.tokobola.wishlist

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.wishlist.domain.WishlistRepository
import com.utsman.tokobola.wishlist.domain.WishlistUseCase
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object WishlistInstanceProvider : SynchronizObject() {

    @Volatile
    private var repository: WishlistRepository? = null
    @Volatile
    private var useCase: WishlistUseCase? = null

    private fun getRepository(): WishlistRepository {
        if (repository == null) repository = WishlistRepository()
        return synchroniz(this) { repository!! }
    }

    fun providedUseCase(): WishlistUseCase {
        if (useCase == null) useCase = WishlistUseCase(getRepository())
        return synchroniz(this) { useCase!! }
    }
}

val LocalWishlistUseCase = compositionLocalOf<WishlistUseCase> { error("Not provided") }