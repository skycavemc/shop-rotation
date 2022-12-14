package de.skycave.shoprotation.codecs

import de.skycave.shoprotation.model.Rewards
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class RewardsCodecProvider : CodecProvider {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> get(clazz: Class<T>?, registry: CodecRegistry?): Codec<T>? {
        registry ?: return null
        if (clazz == Rewards::class.java) {
            return RewardsCodec() as Codec<T>
        }
        return null
    }
}