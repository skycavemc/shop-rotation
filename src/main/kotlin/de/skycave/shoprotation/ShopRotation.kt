package de.skycave.shoprotation

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import de.leonheuer.mcguiapi.gui.GUIFactory
import de.skycave.shoprotation.codecs.ChestCodecProvider
import de.skycave.shoprotation.codecs.ChestItemsCodecProvider
import de.skycave.shoprotation.codecs.LocationCodec
import de.skycave.shoprotation.codecs.RewardsCodecProvider
import de.skycave.shoprotation.command.ShopRotationCommand
import de.skycave.shoprotation.listener.PlayerInteractListener
import de.skycave.shoprotation.model.Chest
import de.skycave.shoprotation.model.ChestItems
import de.skycave.shoprotation.model.Rewards
import de.skycave.shoprotation.utils.CurrentItem
import de.skycave.skycavelib.annotations.InjectService
import de.skycave.skycavelib.annotations.Prefix
import de.skycave.skycavelib.data.MessageRegistry
import de.skycave.skycavelib.models.SkyCavePlugin
import net.milkbowl.vault.economy.Economy
import org.bson.codecs.configuration.CodecRegistries
import org.bukkit.Location
import org.bukkit.Material

@Prefix("&fSky&3Cave &8» ")
class ShopRotation : SkyCavePlugin() {

    val messages = MessageRegistry(this)

    val saveChestsLocation = HashMap<Location, String>()
    val currentItem = HashMap<Material, Int>()

    companion object {
        const val MASTER_VOLUME = 1.0f
    }

    lateinit var mongoClient: MongoClient
        private set
    lateinit var chestItems: MongoCollection<ChestItems>
        private set
    lateinit var chests: MongoCollection<Chest>
        private set
    lateinit var rewards: MongoCollection<Rewards>
        private set
    lateinit var guiFactory: GUIFactory
        private set

    @field:InjectService
    lateinit var economy: Economy
        private set

    override fun onEnable() {
        super.onEnable()

        guiFactory = GUIFactory(this)

        val registry = CodecRegistries.fromRegistries(
            CodecRegistries.fromCodecs(LocationCodec()),
            CodecRegistries.fromProviders(ChestCodecProvider(), ChestItemsCodecProvider(), RewardsCodecProvider()),
            MongoClientSettings.getDefaultCodecRegistry(),
        )
        val settings = MongoClientSettings.builder().codecRegistry(registry).build()
        mongoClient = MongoClients.create(settings)
        val db = mongoClient.getDatabase("shop_rotation")
        chests = db.getCollection("chests", Chest::class.java)
        chestItems = db.getCollection("chest_items", ChestItems::class.java)
        rewards = db.getCollection("rewards", Rewards::class.java)

        registerCommand("shoprotation", ShopRotationCommand(this))
        registerEvents(
            PlayerInteractListener(this)
        )
        registerMessages()
        registerChests()
        CurrentItem.calculateCurrentItem()
    }

    override fun onDisable() {
        mongoClient.close()
    }

    fun registerChests() {
        saveChestsLocation.clear()
        val collection = chests.find()
        for (element in collection) {
            saveChestsLocation[element.location] = element.name
        }
        // TODO get rid of
    }

    private fun registerMessages() {
        val messages = mapOf(
            //global messages
            "no-perms" to "&cDu hast keine Rechte für diesen Befehl.",
            "invalid-number" to "&c%number ist keine gültige Zahl.",
            "invalid-material" to "&cBitte gib ein gültiges Material an.",
            "no-player" to "&cDieser Befehl ist nur für Spieler.",
            "message-unknown" to "&cUnbekannter Befehl. Siehe /shoprotation help",

            //global shoprotation messages
            "unknown-inventory" to "&cDas angefragte Inventar existiert nicht.",
            "not-enough-arguments" to "&cZu wenige Argumente! Siehe /shoprotation help",
            "material-air-not-allowed" to "&cDas Material \"AIR\" ist nicht erlaubt.",

            //chest messages
            "chest-unknown" to "&cDie Chest \"%name\" wurde nicht gefunden.",
            "chest-remove-success" to "&aDie Chest \"%name\" wurde erfolgreich entfernt.",
            "chestitems-is-empty" to "&cEs wurden keine Items in \"%name\" festgelegt.",

            //help messages
            "chest-set-location" to "&e/shoprotation setlocation <name> &8» &8Setzt den Ort für die Chest ",
            "chest-open-gui" to "&e/shoprotation open &8» &8Öffnet das Inventar der Chest ",
            "chest-delete-items" to "&e/shoprotation delete &8» &8Löscht alle Items der Chest ",
            "chest-show-items" to "&e/shoprotation items &8» &8Zeigt alle Items an",
            "chest-show-current-item" to "&e/shoprotation current &8» &8Zeigt das aktuelle Item an",
            "chest-enable" to "&e/shoprotation enable &8» &8Aktiviert die Chest",
            "chest-disable" to "&e/shoprotation disable &8» &8Deaktiviert die Chest",
            "chest-help" to "&e/shoprotation help &8» &8Zeigt diesen Text an",

            //location messages
            "set-location-success" to "&aOrt von \"%name\" wurde erfolgreich gesetzt. &7(%location)",
            "chest-created-success" to "&aChest \"%name\" wurde erstellt. &7(%location)",
            "set-location-syntax" to "&e/shoprotation setlocation <name>",

            //enable-disable messages
            "set-enabled-success" to "&7Die Chest wurde erfolgreich &aAktiviert&7.",
            "enabled-all" to "&7Alle Chests wurden &aaktiviert&7.",
            "set-disabled-success" to "&7Die Chest wurde erfolgreich &cDeaktiviert&7.",
            "disabled-all" to "&7Alle Chests wurden &cdeaktiviert&7.",
            "already-enabled" to "&cDie Chest ist schon &aActiviert&c.",
            "already-disabled" to "&cDie Chest ist schon &4Deaktiviert&c.",
            "set-enabled-syntax" to "&e/shoprotation enable <all/name>",
            "set-disabled-syntax" to "&e/shoprotation disable <all/name>",

            //items messages
            "current-item" to "&eCurrent item: &f %currentitem &7(&f%amount&7/&f%requiredamount&7)",

            //reward messages
            "load-rewards-error" to "&cUnable to load REWARD - DB.",
            "add-item-to-rewards-success" to "&eDas Item (&f&o%material&e, &f&o%amount&e) wurde erfolgreich bei &fBelohnungen &ehinzugefügt.",

            //lootpool messages
            "load-lootpool-error" to "&cUnable to load LOOTPOOL (ChestItems) - DB.",
            "add-item-to-lootpool-success" to "&eDas Item (&f&o%material&e, &f&o%amount&e) wurde erfolgreich bei &fZiele &ehinzugefügt."
        )
        this.messages.registerMany(messages)
    }

}