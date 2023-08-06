package com.utsman.tokobola.database.data

import com.utsman.tokobola.core.utils.nowMillis
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ThumbnailProductRealm : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var productId: Int = 0
    var name: String = ""
    var price: Double = 0.0
    var categoryId: Int = 0
    var categoryName: String = ""
    var brandId: Int = 0
    var brandName: String = ""
    var brandLogo: String = ""
    var image: String = ""
    var promoted: Boolean = false
    var date: Long = nowMillis()
}