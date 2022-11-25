package de.skycave.shoprotation.codecs

import de.skycave.shoprotation.model.Rewards
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import org.bukkit.Material
import java.util.*

class RewardsCodec(codecRegistry: CodecRegistry): Codec<Rewards> {
    private val rewardsCodec: Codec<Rewards>

    init {
        rewardsCodec = codecRegistry.get(Rewards::class.java)
    }

    override fun encode(writer: BsonWriter?, value: Rewards?, encoderContext: EncoderContext?) {
        writer ?: return
        if(value == null) {
            writer.writeNull()
            return
        }
        writer.writeStartDocument()
        writer.writeName("name")
        writer.writeString(value.name)
        writer.writeName("rewardlist")
        writer.writeStartDocument()
        for(reward in value.rewardlist.entries) {
            writer.writeName(reward.key.toString())
            writer.writeInt32(reward.value)
        }
        writer.writeEndDocument()
        writer.writeEndDocument()
        //CORRECT? "writeEndDocument" 2x?
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): Rewards {
        val  rewards = Rewards()
        reader ?: return rewards

        reader.readStartDocument()
        while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            when(reader.readName()) {
                "_id" -> rewards.id = reader.readObjectId()
                "name" -> rewards.name = reader.readString()
                "rewardlist" -> {
                    val rewardlist = EnumMap<Material, Int>(org.bukkit.Material::class.java)
                    while (reader.readBsonType() == BsonType.DOCUMENT) {
                        val type = Material.valueOf(reader.readName())
                        rewardlist[type] = reader.readInt32()
                        reader.readEndDocument()
                    }
                    rewards.rewardlist = rewardlist
                }
                else -> reader.skipValue()
            }
        }
        reader.readEndDocument()
        return rewards
    }

    override fun getEncoderClass(): Class<Rewards> {
        return Rewards::class.java
    }

}