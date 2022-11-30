package de.skycave.shoprotation.utils

import com.mongodb.client.model.Filters
import de.leonheuer.mcguiapi.gui.GUI
import de.leonheuer.mcguiapi.gui.GUIPattern
import de.leonheuer.mcguiapi.utils.ItemBuilder
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.display.CustomSound
import de.skycave.shoprotation.model.display.GUIView
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.plugin.java.JavaPlugin

object UtilsRewards {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun openGUIRewards(player: Player, view: GUIView, args: Array<out String>) {
        val gui = main.guiFactory.createGUI(6, view.getTitle())
        setPresetRewards(player, gui, Material.ORANGE_STAINED_GLASS_PANE)

        when (view) {
            GUIView.REWARDS -> {
                val name = args[2]
                val filter = Filters.eq("name", name)
                val rewards = main.rewards.find(filter).first()
                if(rewards != null) {
                    val rewardlist = rewards.rewardlist.entries
                    var slot = 10
                    for((material, amount) in rewardlist) {
                        if(slot.mod(9) == 0) {
                            slot++
                        } else if((slot + 1).mod(9) == 0) {
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
                val name = args[2]
                val filter = Filters.eq("name", name)
                val rewards = main.rewards.find(filter).first()
                if(rewards != null) {
                    val rewardlist = rewards.rewardlist.entries
                    var slot = 9
                    for((material, amount) in rewardlist) {
                        if(slot.mod(9) == 0) {
                            slot++
                        } else if((slot + 1).mod(9) == 0) {
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
                            openGUIRewards(player, view, args)
                            return@setItem
                        }
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

    private fun setPresetRewards(player: Player, gui: GUI, material: Material) {
        val pattern = GUIPattern.ofPattern("bbbbbbbbb")
            .withMaterial('b', ItemBuilder.of(material).name("§0").asItem())
        gui.formatPattern(pattern.startAtLine(1)).formatPattern(pattern.startAtLine(6))
            .setItem(6,1, ItemBuilder.of(Material.ARROW).name("&cZurück").asItem()) {
                CustomSound.CLICK.playTo(player)
                Utils.openGUI(player, GUIView.MAIN)
            }

        var slot = 9
        while(slot < 54) {
            if(slot.mod(9) == 0) {
                gui.setItem(slot, ItemBuilder.of(material).name("§0").asItem())
            } else if(slot == 17 || slot == 26 || slot == 35 || slot == 44) {
                gui.setItem(slot, ItemBuilder.of(material).name("§0").asItem())
            }
            slot++
        }
    }
}