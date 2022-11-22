package de.skycave.shoprotation.codecs

import de.skycave.shoprotation.model.ChestItems
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ChestItemsCodec(codecRegistry: CodecRegistry): Codec<ChestItems> {
    private val itemStackCodec: Codec<ItemStack>

    init {
        itemStackCodec = codecRegistry.get(ItemStack::class.java)
    }

    override fun encode(writer: BsonWriter?, value: ChestItems?, encoderContext: EncoderContext?) {
        writer ?: return
        if (value == null) {
            writer.writeNull()
            return
        }

        writer.writeStartDocument()
        writer.writeName("name")
        writer.writeString(value.name)
        writer.writeName("items")
        writer.writeStartDocument()
        for (item in value.items.entries) {
            writer.writeName(item.key.toString())
            writer.writeInt32(item.value)
        }
        writer.writeEndDocument()
        writer.writeEndDocument()
        //CORRECT? "writeEndDocument" 2x?
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): ChestItems {
        val chestitems = ChestItems()
        reader ?: return chestitems

        reader.readStartDocument()
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            when (reader.readName()) {
                "_id" -> chestitems.id = reader.readObjectId()
                "name" -> chestitems.name = reader.readString()
                "items" -> {
                    val items = HashMap<Material, Int>()
                    while (reader.readBsonType() == BsonType.DOCUMENT) {
                        reader.readStartDocument()
                        val type = Material.valueOf(reader.readName())
                        items[type] = reader.readInt32()
                        reader.readEndDocument()
                    }
                    chestitems.items = items
                }
                else -> reader.skipValue()
            }
        }
        reader.readEndDocument()
        return chestitems
    }

    override fun getEncoderClass(): Class<ChestItems> {
        return ChestItems::class.java
    }
}