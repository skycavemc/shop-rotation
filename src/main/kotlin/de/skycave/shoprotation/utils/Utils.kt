package de.skycave.shoprotation.utils

import de.leonheuer.mcguiapi.gui.GUIPattern
import de.leonheuer.mcguiapi.utils.ItemBuilder
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.Rewards
import de.skycave.shoprotation.model.display.CustomSound
import de.skycave.shoprotation.model.display.GUIView
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object Utils {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun openGUI(player: Player, view: GUIView) {
        val gui = main.guiFactory.createGUI(6, view.getTitle())

        if(view != GUIView.MAIN) {
            val pattern = GUIPattern.ofPattern("bbbbbbbbb")
                .withMaterial('b', ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name("§0").asItem())
            gui.formatPattern(pattern.startAtLine(1)).formatPattern(pattern.startAtLine(6))
                .setItem(6,1, ItemBuilder.of(Material.ARROW).name("&cZurück").asItem()) {
                    CustomSound.CLICK.playTo(player)
                    openGUI(player, GUIView.MAIN)
                }
        }

        if(view == GUIView.REWARDS) {
            var rewards = main.rewards.find().first()
            if(rewards != null) {
                rewards = Rewards()
                val rewardlist = rewards.rewardlist.entries
                var slot = 9
                for(r in rewardlist) {
                    if(slot.mod(9) == 0) {
                        slot ++
                    } else if((slot + 1).mod(9) == 0) {
                        slot += 2
                    }
                }

            }
        }
    }
}