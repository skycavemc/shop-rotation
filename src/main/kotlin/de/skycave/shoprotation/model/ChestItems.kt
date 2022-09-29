package de.skycave.shoprotation.model


import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.bukkit.inventory.ItemStack

class ChestItems {

    @BsonId
    lateinit var id: ObjectId
    lateinit var name: String
    lateinit var items: Map<ItemStack, Int>

    constructor()

    constructor(id: ObjectId, name: String, items: Map<ItemStack, Int>) {
        this.id = id
        this.name = name
        this.items = items

    }

}