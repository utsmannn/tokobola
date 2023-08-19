package com.utsman.tokobola.database.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class LocationPlaceRealm : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var key: String = ""
    var name: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var bbox: String = ""
}