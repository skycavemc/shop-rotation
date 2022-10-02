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