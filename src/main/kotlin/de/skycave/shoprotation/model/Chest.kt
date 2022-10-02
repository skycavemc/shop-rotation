package de.skycave.shoprotation.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.util.concurrent.atomic.AtomicInteger

class Chest {

    @BsonId
    lateinit var id: ObjectId
    lateinit var name: String
    lateinit var location: Location
    lateinit var amount: Single<Int>
    lateinit var item: ItemStack

    constructor()

    constructor(id: ObjectId, name: String, location: Location, amount: Single<Int>, item: ItemStack) {
        this.id = id
        this.name = name
        this.location = location
        this.amount = amount
        this.item = item
    }
}