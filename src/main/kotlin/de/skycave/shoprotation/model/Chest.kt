package de.skycave.shoprotation.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.bukkit.Location
import org.bukkit.Material
import kotlin.properties.Delegates

class Chest {

    @BsonId
    lateinit var id: ObjectId
    lateinit var name: String
    lateinit var location: Location
    var enabled by Delegates.notNull<Boolean>()
    lateinit var item: Material
    var amount by Delegates.notNull<Int>()
    var requiredAmount by Delegates.notNull<Int>()
    lateinit var lootpool: List<String>

    constructor()

    constructor(
        id: ObjectId,
        name: String,
        location: Location,
        enabled: Boolean,
        item: Material,
        amount: Int,
        requiredAmount: Int,
        lootpool: List<String>
    ) {
        this.id = id
        this.name = name
        this.location = location
        this.enabled = enabled
        this.item = item
        this.amount = amount
        this.requiredAmount = requiredAmount
        this.lootpool = lootpool
    }
}