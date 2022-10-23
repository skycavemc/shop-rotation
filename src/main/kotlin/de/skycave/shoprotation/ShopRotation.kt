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
            CodecRegistries.fromCodecs(LocationCodec()),
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

            "chest-unknown" to "&cDie Chest %name wurde nicht gefunden.",

            //help messages
            "chest-set-location" to "&e/shoprotation setlocation <name> &8» &8Setzt die Location für die Chest ",
            "chest-open-gui" to "&e/shoprotation open &8» &8Öffnet das Inventar der Chest ",
            "chest-delete-items" to "&e/shoprotation delete &8» &8Löscht alle Items der Chest ",
            "chest-show-items" to "&e/shoprotation items &8» &8Zeigt alle Items an",
            "chest-show-current-item" to "&e/shoprotation current &8» &8Zeigt das aktuelle Item an",
            "chest-enable" to "&e/shoprotation enable &8» &8Aktiviert die Chest",
            "chest-disable" to "&e/shoprotation disable &8» &8Deaktiviert die Chest",
            "chest-help" to "&e/shoprotation help &8» &8Zeigt diesen Text an",

            //location messages
            "set-location-success" to "&aOrt wurde erfolgreich gesetzt. &7(%x, %y, %z, %direction)",
            "set-location-syntax" to "&e/shoprotation setlocation <name>",

            //enable-disable messages
            "set-enabled-success" to "&cDie Kiste wurde erfolgreich &aAktiviert&c.",
            "enabled-all" to "&7Alle Chests wurden &aaktiviert&7.",
            "set-disabled-success" to "&cDie Kiste wurde erfolgreich &4Deaktiviert&c.",
            "disabled-all" to "&7Alle Chests wurden &cdeaktiviert&7.",
            "already-enabled" to "&cDie Kiste ist schon &aActiviert&c.",
            "already-disabled" to "&cDie Kiste ist schon &4Deaktiviert&c.",
            "set-enabled-syntax" to "&e/shoprotation enable <all/name>",
            "set-disabled-syntax" to "&e/shoprotation disable <all/name>",

            //TODO: Kisten -> Chests ändern
        )
        this.messages.registerMany(messages)
    }

}