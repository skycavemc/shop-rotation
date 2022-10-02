package de.skycave.shoprotation.codecs

import de.skycave.shoprotation.model.ChestItems
import de.skycave.shoprotation.model.Single
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bukkit.inventory.ItemStack

@Suppress("UNCHECKED_CAST")
class SingleIntCodec: Codec<Single<Int>> {
    override fun encode(writer: BsonWriter?, value: Single<Int>?, encoderContext: EncoderContext?) {
        writer ?: return
        if (value == null) {
            writer.writeNull()
            return
        }
        writer.writeInt32(value.value)
    }

    override fun getEncoderClass(): Class<Single<Int>> {
        return Single::class.java as Class<Single<Int>>
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): Single<Int> {
        reader ?: return Single(-1)
        return Single(reader.readInt32())
    }

}