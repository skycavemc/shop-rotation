package de.skycave.shoprotation.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.bukkit.Location
import org.bukkit.Material

class Chest() {

    @BsonId
    lateinit var id: ObjectId
    lateinit var name: String
    lateinit var location: String
    var enabled = false
    var item: Material? = null
    var amount = 0
    var requiredAmount = 0
    var lootpool = ArrayList<String>()

}