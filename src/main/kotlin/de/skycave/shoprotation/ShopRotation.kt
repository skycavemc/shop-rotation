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
import de.skycave.skycavelib.models.SkyCavePlugin
import net.milkbowl.vault.economy.Economy
import org.bson.codecs.configuration.CodecRegistries
import org.bukkit.Material

@Prefix("&fSky&3Cave &8» ")
class ShopRotation : SkyCavePlugin() {

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
        CurrentItem.calculateCurrentItem()
    }

    override fun onDisable() {
        mongoClient.close()
    }

}