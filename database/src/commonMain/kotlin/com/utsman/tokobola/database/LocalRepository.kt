package com.utsman.tokobola.database

import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import com.utsman.tokobola.core.utils.asyncAwait
import com.utsman.tokobola.core.utils.nowMillis
import com.utsman.tokobola.database.data.RecentlyViewedRealm
import com.utsman.tokobola.database.data.ThumbnailProductRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.query.find
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

class LocalRepository(private val realm: Realm) {

    suspend fun insertThumbnailProduct(thumbnailProductRealm: ThumbnailProductRealm) {
        asyncAwait {
            val isProductExist = isExistingThumbnailProduct(thumbnailProductRealm.productId)
            if (isProductExist) {
                realm.write {
                    query(
                        ThumbnailProductRealm::class,
                        "productId == ${thumbnailProductRealm.productId}"
                    )
                        .find {
                            delete(it.first())
                        }
                }
            }

            realm.write {
                copyToRealm(thumbnailProductRealm, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    suspend fun selectAllThumbnailProduct(): List<ThumbnailProductRealm> {
        return asyncAwait {
            realm.query(ThumbnailProductRealm::class).find().reversed()
        }
    }

    suspend fun selectAllThumbnailProductFlow(): Flow<List<ThumbnailProductRealm>> {
        return asyncAwait {
            realm.query(ThumbnailProductRealm::class).asFlow()
                .map { it.list.reversed() }
        }
    }

    suspend fun selectByIdThumbnailProduct(id: Int): ThumbnailProductRealm? {
        return asyncAwait {
            realm.query(ThumbnailProductRealm::class, "productId == $id")
                .first()
                .find()
        }
    }

    suspend fun isExistingThumbnailProduct(id: Int): Boolean {
        return asyncAwait {
            realm.query(ThumbnailProductRealm::class, "productId == $id")
                .first()
                .find() != null
        }
    }

    suspend fun insertRecentlyViewed(recentlyViewedRealm: RecentlyViewedRealm) {
        asyncAwait {
            val isExist = realm.query(RecentlyViewedRealm::class, "productId == ${recentlyViewedRealm.productId}")
                .first()
                .find() != null

            if (isExist) {
                realm.write {
                    query(RecentlyViewedRealm::class, "productId == ${recentlyViewedRealm.productId}")
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
                        ThumbnailProductRealm::class,
                        RecentlyViewedRealm::class
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