package de.skycave.shoprotation.codecs

import de.skycave.shoprotation.model.Chest
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import org.bukkit.Location
import org.bukkit.Material

class ChestCodec(codecRegistry: CodecRegistry) : Codec<Chest> {

    private val locationCodec: Codec<Location>

    init {
        locationCodec = codecRegistry.get(Location::class.java)
    }

    override fun encode(writer: BsonWriter?, value: Chest?, encoderContext: EncoderContext?) {
        writer ?: return
        if (value == null) {
            writer.writeNull()
            return
        }

        writer.writeStartDocument()
        writer.writeName("name")
        writer.writeString(value.name)
        writer.writeName("location")
        locationCodec.encode(writer, value.location, encoderContext)
        writer.writeName("enabled")
        writer.writeBoolean(value.enabled)
        writer.writeName("item")
        writer.writeString(value.item.toString())
        writer.writeName("amount")
        writer.writeInt32(value.amount)
        writer.writeName("required_amount")
        writer.writeInt32(value.requiredAmount)
        writer.writeName("lootpool")
        writer.writeStartArray()
        value.lootpool.forEach{writer.writeString(it)}
        writer.writeEndArray()
        writer.writeEndDocument()
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): Chest {
        val chest = Chest()
        reader ?: return chest

        reader.readStartDocument()
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            when (reader.readName()) {
                "_id" -> chest.id = reader.readObjectId()
                "name" -> chest.name = reader.readString()
                "location" -> chest.location = locationCodec.decode(reader, decoderContext)
                "enabled" -> chest.enabled = reader.readBoolean()
                "item" -> chest.item = Material.valueOf(reader.readString())
                "amount" -> chest.amount = reader.readInt32()
                "required_amount" -> chest.requiredAmount = reader.readInt32()
                "lootpool" -> {
                    val lootpool = ArrayList<String>()
                    reader.readStartArray()
                    while (reader.readBsonType() == BsonType.STRING) {
                        lootpool.add(reader.readString())
                    }
                    reader.readEndArray()
                }
                else -> reader.skipValue()
            }
        }
        reader.readEndDocument()
        return chest
    }

    override fun getEncoderClass(): Class<Chest> {
        return Chest::class.java
    }
}