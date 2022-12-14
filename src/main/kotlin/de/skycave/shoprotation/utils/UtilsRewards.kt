package de.skycave.shoprotation.utils

import com.mongodb.client.model.Filters
import de.leonheuer.mcguiapi.utils.ItemBuilder
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.display.GUIView
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.plugin.java.JavaPlugin

object UtilsRewards {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun openGUIRewards(player: Player, view: GUIView, name: String) {
        val gui = main.guiFactory.createGUI(6, view.getTitle())
        Utils.setPresetBorder(gui, Material.ORANGE_STAINED_GLASS_PANE)

        when (view) {
            GUIView.REWARDS -> {
                val filter = Filters.eq("name", name)
                val rewards = main.rewards.find(filter).first()
                if (rewards != null) {
                    val rewardlist = rewards.rewardlist.entries
                    var slot = 10
                    for ((material, amount) in rewardlist) {
                        if (slot.mod(9) == 0) {
                            slot++
                        } else if ((slot + 1).mod(9) == 0) {
                            slot += 2
                        }
                        val item = ItemBuilder.of(material)
                            .amount(amount)
                            .name("&f&nItem&f: &e${material}&f, &nAnzahl&f: &e$amount")
                            .asItem()
                        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                        gui.setItem(slot, item)
                        slot++
                    }
                }
                gui.show(player)
                return
            }

            GUIView.REWARDS_REMOVE -> {
                val filter = Filters.eq("name", name)
                val rewards = main.rewards.find(filter).first()
                if (rewards != null) {
                    val rewardlist = rewards.rewardlist.entries
                    var slot = 10
                    for ((material, amount) in rewardlist) {
                        if (slot.mod(9) == 0) {
                            slot++
                        } else if ((slot + 1).mod(9) == 0) {
                            slot += 2
                        }
                        val item = ItemBuilder.of(material)
                            .amount(amount)
                            .name("&f&nItem&f: &e${material}&f, &nAnzahl&f: &e$amount")
                            .description("&7Zum Entfernen klicken!")
                            .asItem()
                        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                        gui.setItem(slot, item) {
                            rewards.rewardlist.remove(material)
                            main.rewards.replaceOne(filter, rewards)
                            openGUIRewards(player, GUIView.REWARDS_REMOVE, name)
                            return@setItem
                        }
                        slot++
                    }
                }
                gui.show(player)
                return
            }

            else -> {
                main.messages.get("unknown-inventory").send(player)
                return
            }
        }
    }

}