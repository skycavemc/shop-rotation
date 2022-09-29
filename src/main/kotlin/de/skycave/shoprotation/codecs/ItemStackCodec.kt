package de.skycave.shoprotation.codecs

import org.bson.BsonBinary
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemStackCodec: Codec<ItemStack> {

    override fun encode(writer: BsonWriter?, value: ItemStack?, encoderContext: EncoderContext?) {
        writer ?: return
        if (value == null) {
            writer.writeNull()
            return
        }
        writer.writeBinaryData(BsonBinary(value.serializeAsBytes()))
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): ItemStack {
        reader ?: return ItemStack(Material.AIR, 0)
        return ItemStack.deserializeBytes(reader.readBinaryData().data)
    }

    override fun getEncoderClass(): Class<ItemStack> {
        return ItemStack::class.java
    }

}