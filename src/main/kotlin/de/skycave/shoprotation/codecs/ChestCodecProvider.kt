package de.skycave.shoprotation.codecs

import de.skycave.shoprotation.model.Chest
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class ChestCodecProvider: CodecProvider {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> get(clazz: Class<T>?, registry: CodecRegistry?): Codec<T>? {
        registry ?: return null
        if (clazz == Chest::class.java) {
            return ChestCodec(registry) as Codec<T>
        }
        return null
    }
}