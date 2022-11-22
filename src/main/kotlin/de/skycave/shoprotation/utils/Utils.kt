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
                .withMaterial('b', ItemBuilder.of(Material.CYAN_STAINED_GLASS_PANE).name("§0").asItem())
            gui.formatPattern(pattern.startAtLine(1)).formatPattern(pattern.startAtLine(6))
                .setItem(6,1, ItemBuilder.of(Material.ARROW).name("&cZurück").asItem()) {
                    CustomSound.CLICK.playTo(player)
                    openGUI(player, GUIView.MAIN)
                }
                //TODO: SET DESCRIPTIONS FOR ITEMS
                .setItem(6,4, ItemBuilder.of(Material.NETHER_STAR).name("&dBelohnungen").asItem()) {
                    if(!player.hasPermission("skybee.shoprotation.rewards.show")) {
                        main.messages.get("no-perms").send(player)
                        CustomSound.ERROR.playTo(player)
                        return@setItem
                    }
                    CustomSound.CLICK.playTo(player)
                    openGUI(player, GUIView.REWARDS)
                }
                .setItem(6, 5, ItemBuilder.of(Material.OAK_SIGN).name("&eHilfe!").asItem()) {
                    sendHelp(player)
                    player.closeInventory()
                }
                .setItem(6,6, ItemBuilder.of(Material.WHITE_SHULKER_BOX).name("&eZiele!").asItem()) {
                    if(!player.hasPermission("skybee.shoprotation.lootpool.show")) {
                        main.messages.get("no-perms").send(player)
                        CustomSound.ERROR.playTo(player)
                        return@setItem
                    }
                    CustomSound.CLICK.playTo(player)
                    openGUI(player, GUIView.LOOTPOOL)
                }
        }

        when (view) {
            GUIView.REWARDS -> {
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
            GUIView.REWARDS_REMOVE -> {

            }
            GUIView.LOOTPOOL -> {

            }
            GUIView.LOOTPOOL_REMOVE -> {

            }
            GUIView.MAIN -> {

            }
        }
        gui.show(player)
    }

    private fun sendHelp(player: Player) {
        main.messages.get("chest-set-location").send(player)
        main.messages.get("chest-open-gui").send(player)
        main.messages.get("chest-delete-items").send(player)
        main.messages.get("chest-show-items").send(player)
        main.messages.get("chest-show-current-item").send(player)
        main.messages.get("chest-enable").send(player)
        main.messages.get("chest-disable").send(player)
        main.messages.get("chest-help").send(player)
    }
}