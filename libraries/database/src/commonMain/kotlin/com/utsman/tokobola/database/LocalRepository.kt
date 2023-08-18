package com.utsman.tokobola.database

import com.utsman.tokobola.core.SingletonCreator
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.core.utils.asyncAwait
import com.utsman.tokobola.database.data.CartProductRealm
import com.utsman.tokobola.database.data.RecentlyViewedRealm
import com.utsman.tokobola.database.data.WishlistRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.query.find
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

class LocalRepository(private val realm: Realm) {

    init {
        println("create local repository......")
    }

    suspend fun insertRecentlyViewed(recentlyViewedRealm: RecentlyViewedRealm) {
        asyncAwait {
            val isExist = realm.query(
                RecentlyViewedRealm::class,
                "productId == ${recentlyViewedRealm.productId}"
            )
                .first()
                .find() != null

            if (isExist) {
                realm.write {
                    query(
                        RecentlyViewedRealm::class,
                        "productId == ${recentlyViewedRealm.productId}"
                    )
                        .find {
                            delete(it.first())
                        }
                }
            }

            realm.write {
                copyToRealm(recentlyViewedRealm, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    suspend fun selectAllRecentlyViewed(): Flow<List<RecentlyViewedRealm>> {
        return asyncAwait {
            realm.query(RecentlyViewedRealm::class).asFlow()
                .map { it.list.asReversed().take(10) }
        }
    }

    suspend fun insertOrUpdateProductCart(productId: Int, operationQuantity: (Int) -> Int) {
        asyncAwait {
            val productFound = realm.query(CartProductRealm::class, "productId == $productId")
                .first()
                .find()

            val quantity = if (productFound != null) {
                val currentQuantity = productFound.quantity
                val newQuantity = operationQuantity.invoke(currentQuantity)

                realm.write {
                    query(CartProductRealm::class, "productId == $productId")
                        .find {
                            delete(it.first())
                        }
                }

                newQuantity
            } else {
                operationQuantity.invoke(0)
            }

            realm.write {
                val newCartProductRealm = CartProductRealm().also {
                    it.productId = productId
                    it.quantity = quantity
                }
                copyToRealm(newCartProductRealm)
            }
        }
    }

    suspend fun getProductCart(productId: Int): Flow<CartProductRealm?> {
        return asyncAwait {
            realm.query(CartProductRealm::class, "productId == $productId")
                .asFlow().map { it.list.firstOrNull() }
        }
    }

    suspend fun replaceAllCart(list: List<CartProductRealm>) {
        asyncAwait {
            realm.write {
                val all = query(CartProductRealm::class)
                delete(all)

                list
                    .filter { it.quantity > 0 }
                    .forEach {
                    copyToRealm(it, updatePolicy = UpdatePolicy.ALL)
                }

            }
        }
    }

    suspend fun selectAllCart(): Flow<List<CartProductRealm>> {
        return asyncAwait {
            realm.query(CartProductRealm::class).asFlow()
                .map { it.list.sortedBy { cart -> cart.millis }.asReversed() }
        }
    }

    suspend fun toggleWishlist(wishlistRealm: WishlistRealm) {
        asyncAwait {
            val isExist = realm.query(
                WishlistRealm::class,
                "productId == ${wishlistRealm.productId}"
            )
                .first()
                .find() != null

            if (isExist) {
                realm.write {
                    query(
                        WishlistRealm::class,
                        "productId == ${wishlistRealm.productId}"
                    )
                        .find {
                            delete(it.first())
                        }
                }
            } else {
                realm.write {
                    copyToRealm(wishlistRealm, updatePolicy = UpdatePolicy.ALL)
                }
            }
        }
    }

    suspend fun checkWishlistIsExist(productId: Int): Flow<Boolean> {
        return asyncAwait {
            realm.query(
                WishlistRealm::class,
                "productId == $productId"
            )
                .asFlow()
                .map { it.list.firstOrNull() != null }
        }
    }

    suspend fun selectAllWishlist(): Flow<List<WishlistRealm>> {
        return asyncAwait {
            realm.query(WishlistRealm::class).asFlow()
                .map { it.list.asReversed() }
        }
    }

    @ThreadLocal
    companion object : SynchronizObject() {

        @Volatile
        private var realm: Realm? = null

        private fun providedRealmProduct(): Realm {
            return synchroniz(this) {
                if (realm == null) {
                    val config = RealmConfiguration.create(
                        schema = setOf(
                            RecentlyViewedRealm::class,
                            CartProductRealm::class,
                            WishlistRealm::class,
                            // add others if needed
                        )
                    )
                    realm = Realm.open(config)
                }
                realm as Realm
            }
        }

        fun providedLocalRepository(): LocalRepository {
            return Builder.create { LocalRepository(providedRealmProduct()) }
        }
    }

    private object Builder : SingletonCreator<LocalRepository>()
}