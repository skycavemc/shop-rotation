package de.skycave.shoprotation.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.bukkit.Material

class Rewards {

    @BsonId
    lateinit var id: ObjectId
    lateinit var name: String
    lateinit var rewardlist: Map<Material, Int>

    constructor()

    constructor(id: ObjectId, name: String, rewardlist: Map<Material, Int>) {
        this.id = id
        this.name = name
        this.rewardlist = rewardlist
    }

}