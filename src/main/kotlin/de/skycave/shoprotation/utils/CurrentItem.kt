package de.skycave.shoprotation.utils

import com.mongodb.client.model.Filters
import de.skycave.shoprotation.ShopRotation
import org.bukkit.plugin.java.JavaPlugin

object CurrentItem {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun calculateCurrentItem() {
        val collection = main.chests.find()
        for(element in collection) {
            val name = element.name
            val filter = Filters.eq("name", name)
            val chest = main.chests.find(filter).first()
            val chestItems = main.chestItems.find(filter).first()
            if (chest != null) {
                if (chest.item == null) {
                    if (chestItems != null) {
                        if (chestItems.items.isEmpty()) {
                            val noItems = "[ShopRotation] Die Chest \"%name\" enthaelt keine Items!".replace("%name", name)
                            println(noItems)
                            return
                        } else {
                            val item = chestItems.items
                            //TODO: Random Item out of ChestItems EnumMap
                        }
                    }
                }
            }
        }
    }
}