package de.skycave.shoprotation.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.bukkit.Location
import java.util.concurrent.atomic.AtomicInteger

class Chest {

    @BsonId
    lateinit var id: ObjectId
    lateinit var name: String
    lateinit var location: Location
    @BsonProperty(value = "current_amount") lateinit var currentAmount: Single<Int>

    constructor()

    constructor(id: ObjectId, name: String, location: Location, currentAmount: Single<Int>) {
        this.id = id
        this.name = name
        this.location = location
        this.currentAmount = currentAmount
    }
}