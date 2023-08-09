package com.utsman.tokobola.database

import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.core.utils.asyncAwait
import com.utsman.tokobola.database.data.CartProductRealm
import com.utsman.tokobola.database.data.RecentlyViewedRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.query.find
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

class LocalRepository(private val realm: Realm) {

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
                .map { it.list.asReversed() }
        }
    }

    suspend fun insertOrUpdateProductCart(productId: Int, operationQuantity: (Int) -> Int) {
        println("updated.....")
        asyncAwait {
            val productFound = realm.query(CartProductRealm::class, "productId == $productId")
                .first()
                .find()

            val quantity = if (productFound != null) {
                println("not null..... $productFound")
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

            println("inserted.....")
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
            val anu = realm.query(CartProductRealm::class, "productId == $productId")
                .asFlow().mapLatest { it.list.firstOrNull() }
            anu
        }
    }

    @ThreadLocal
    companion object : SynchronizObject() {

        @Volatile
        private var realm: Realm? = null

        @Volatile
        private var repository: LocalRepository? = null

        private fun providedRealmProduct(): Realm {
            if (realm == null) {
                val config = RealmConfiguration.create(
                    schema = setOf(
                        RecentlyViewedRealm::class,
                        CartProductRealm::class
                    )
                ) // add others if needed
                realm = Realm.open(config)
            }

            return synchroniz(this) { realm!! }
        }

        fun providedLocalRepository(): LocalRepository {
            if (repository == null) repository = LocalRepository(providedRealmProduct())
            return synchroniz(this) { repository!! }
        }
    }
}