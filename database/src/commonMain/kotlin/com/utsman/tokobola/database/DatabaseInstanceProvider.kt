package com.utsman.tokobola.database

import androidx.compose.runtime.compositionLocalOf
import com.utsman.tokobola.core.SynchronizObject
import com.utsman.tokobola.core.synchroniz
import io.realm.kotlin.Realm
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object DatabaseInstanceProvider : SynchronizObject() {

    @Volatile
    private var localRepository: LocalRepository? = null

    // val config = RealmConfiguration.create(schema = setOf(Item::class))
    //val realm: Realm = Realm.open(config)



    fun providedDatabaseRepository(realm: Realm): LocalRepository {
        if (localRepository == null) localRepository = LocalRepository(realm)
        return synchroniz(this) { localRepository!! }
    }
}

val LocalLocalRepository = compositionLocalOf<LocalRepository> { error("Not provided") }