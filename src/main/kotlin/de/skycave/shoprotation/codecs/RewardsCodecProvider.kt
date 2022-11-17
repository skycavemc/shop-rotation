package de.skycave.shoprotation.codecs

import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class RewardsCodecProvider: CodecProvider {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> get(clazz: Class<T>?, registry: CodecRegistry?): Codec<T>? {
        registry ?: return null
        if (clazz == RewardsCodec::class.java) {
            return RewardsCodec(registry) as Codec<T>
        }
        return null
    }
}