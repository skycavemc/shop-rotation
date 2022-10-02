package de.skycave.shoprotation

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import de.skycave.shoprotation.codecs.*
import de.skycave.shoprotation.command.ShopRotationCommand
import de.skycave.shoprotation.model.Chest
import de.skycave.shoprotation.model.ChestItems
import de.skycave.skycavelib.annotations.Prefix
import de.skycave.skycavelib.data.MessageRegistry
import de.skycave.skycavelib.models.SkyCavePlugin
import org.bson.codecs.configuration.CodecRegistries

@Prefix("&fSky&3Cave &8» ")
class ShopRotation : SkyCavePlugin() {

    val messages = MessageRegistry(this)

    lateinit var mongoClient: MongoClient
        private set
    lateinit var chestItems: MongoCollection<ChestItems>
        private set
    lateinit var chests: MongoCollection<Chest>
        private set

    override fun onEnable() {
        val registry = CodecRegistries.fromRegistries(
            CodecRegistries.fromCodecs(LocationCodec(), SingleIntCodec()),
            CodecRegistries.fromProviders(ChestCodecProvider(), ChestItemsCodecProvider())
        )
        val settings = MongoClientSettings.builder().codecRegistry(registry).build()
        mongoClient = MongoClients.create(settings)
        val db = mongoClient.getDatabase("shop_rotation")
        chests = db.getCollection("chests", Chest::class.java)
        chestItems = db.getCollection("chest_items", ChestItems::class.java)

        registerCommand("shoprotation", ShopRotationCommand(this))
        registerEvents()
    }

    override fun onDisable() {
    }

    private fun registerMessages() {
        val messages = mapOf(
            //global messages
            "no-perms" to "&cDu hast keine Rechte für diesen Befehl.",
            "invalid-number" to "&c%number ist keine gültige Zahl.",
            "invalid-material" to "&cBitte gib ein gültiges Material an.",
            "no-player" to "&cDieser Befehl ist nur für Spieler.",
            "message-unknown" to "&cUnbekannter Befehl. Siehe /shoprotation help",

            //info messages

            //location messages
            "set-location-success" to "&aOrt wurde erfolgreich gesetzt. &7(%x, %y, %z, %direction)",
        )
        this.messages.registerMany(messages)
    }

}