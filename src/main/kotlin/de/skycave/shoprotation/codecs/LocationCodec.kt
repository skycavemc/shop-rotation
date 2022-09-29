package de.skycave.shoprotation.codecs

import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

class LocationCodec: Codec<Location> {

    override fun encode(writer: BsonWriter?, value: Location?, encoderContext: EncoderContext?) {
        writer ?: return
        if (value == null) {
            writer.writeNull()
            return
        }

        writer.writeStartDocument()
        writer.writeName("x")
        writer.writeDouble(value.x)
        writer.writeName("y")
        writer.writeDouble(value.y)
        writer.writeName("z")
        writer.writeDouble(value.z)
        writer.writeName("yaw")
        writer.writeDouble(value.yaw.toDouble())
        writer.writeName("pitch")
        writer.writeDouble(value.pitch.toDouble())
        writer.writeName("world")
        writer.writeString(value.world.uid.toString())
        writer.writeEndDocument()
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): Location {
        val location = Location(null, 0.0, 0.0, 0.0)
        reader ?: return location

        reader.readStartDocument()
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            when (reader.readName()) {
                "x" -> location.x = reader.readDouble()
                "y" -> location.y = reader.readDouble()
                "z" -> location.z = reader.readDouble()
                "yaw" -> location.yaw = reader.readDouble().toFloat()
                "pitch" -> location.pitch = reader.readDouble().toFloat()
                "world" -> location.world = Bukkit.getWorld(UUID.fromString(reader.readString()))
                else -> reader.skipValue()
            }
        }
        reader.readEndDocument()
        return location
    }

    override fun getEncoderClass(): Class<Location> {
        return Location::class.java
    }

}