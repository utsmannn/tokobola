package com.utsman.tokobola.database.data

import com.utsman.tokobola.core.utils.nowMillis
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class RecentlyViewedRealm : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var productId: Int = 0
    var date: Long = nowMillis()
}