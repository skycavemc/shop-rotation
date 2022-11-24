package de.skycave.shoprotation.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.bukkit.Material
import java.util.*

class ChestItems {

    @BsonId
    lateinit var id: ObjectId
    lateinit var name: String
    var items: EnumMap<Material, Int> = EnumMap(org.bukkit.Material::class.java)

    constructor()

    constructor(id: ObjectId, name: String) {
        this.id = id
        this.name = name
    }
}