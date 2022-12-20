package de.skycave.shoprotation.utils

import com.mongodb.client.model.Filters
import de.leonheuer.mcguiapi.utils.ItemBuilder
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.enums.Message
import de.skycave.shoprotation.model.display.GUIView
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.plugin.java.JavaPlugin

object UtilsChestItems {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun openGUIChestItems(player: Player, view: GUIView, name: String) {
        val gui = main.guiFactory.createGUI(6, view.getTitle())
        Utils.setPresetBorder(gui, Material.PINK_STAINED_GLASS_PANE)

        when (view) {
            GUIView.LOOTPOOL -> {
                val filter = Filters.eq("name", name)
                val chestItems = main.chestItems.find(filter).first()
                if (chestItems != null) {
                    val chestitemslist = chestItems.items.entries
                    var slot = 9
                    for ((material, amount) in chestitemslist) {
                        if (slot.mod(9) == 0) {
                            slot++
                        } else if ((slot + 1).mod(9) == 0) {
                            slot += 2
                        }
                        var amountfinal = amount
                        if (amount > 64) {
                            amountfinal = 64
                        }
                        val item = ItemBuilder.of(material)
                            .amount(amountfinal)
                            .name("&f&nItem&f: &e${material}&f, &nBenötigte Menge&f: &e$amountfinal")
                            .asItem()
                        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                        gui.setItem(slot, item)
                    }
                }
                gui.show(player)
                return
            }

            GUIView.LOOTPOOL_REMOVE -> {
                val filter = Filters.eq("name", name)
                val chestItems = main.chestItems.find(filter).first()
                if (chestItems != null) {
                    val chestitemslist = chestItems.items.entries
                    var slot = 9
                    for ((material, amount) in chestitemslist) {
                        if (slot.mod(9) == 0) {
                            slot++
                        } else if ((slot + 1).mod(9) == 0) {
                            slot += 2
                        }
                        var amountfinal = amount
                        if (amount > 64) {
                            amountfinal = 64
                        }
                        val item = ItemBuilder.of(material)
                            .amount(amountfinal)
                            .name("&f&nItem&f: &e${material}&f, &nBenötigte Menge&f: &e$amount")
                            .description("&7Zum Entfernen klicken!")
                            .asItem()
                        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                        gui.setItem(slot, item) {
                            chestItems.items.remove(material)
                            openGUIChestItems(player, GUIView.LOOTPOOL_REMOVE, name)
                            return@setItem
                        }
                    }
                }
                gui.show(player)
                return
            }

            else -> {
                Message.UNKNOWN_INVENTORY.get().send(player)
                return
            }
        }
    }

}