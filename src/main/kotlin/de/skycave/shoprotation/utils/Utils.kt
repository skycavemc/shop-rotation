package de.skycave.shoprotation.utils

import de.leonheuer.mcguiapi.gui.GUI
import de.leonheuer.mcguiapi.gui.GUIPattern
import de.leonheuer.mcguiapi.utils.ItemBuilder
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.display.CustomSound
import de.skycave.shoprotation.model.display.GUIView
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object Utils {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun openGUI(player: Player, view: GUIView, args: Array<out String>) {
        if(view == GUIView.MAIN) {
            //TODO: Main GUI Inventory here
            val gui = main.guiFactory.createGUI(6, GUIView.MAIN.toString())
            setPresetMain(player, gui, Material.CYAN_STAINED_GLASS_PANE, args)
        }
    }

    private fun setPresetMain(player: Player, gui: GUI, material: Material, args: Array<out String>) {
        val pattern = GUIPattern.ofPattern("bbbbbbbbb")
            .withMaterial('b', ItemBuilder.of(material).name("§0").asItem())
        gui.formatPattern(pattern.startAtLine(1)).formatPattern(pattern.startAtLine(6))

            .setItem(6,1, ItemBuilder.of(Material.ARROW).name("&cZurück").asItem()) {
                CustomSound.CLICK.playTo(player)
                openGUI(player, GUIView.MAIN, args)
            }

            .setItem(6,4, ItemBuilder.of(Material.NETHER_STAR)
                .name("&dBelohnungen")
                .description("&7Zeigt alle möglichen Belohnungen an!")
                .asItem()) {
                if(!player.hasPermission("skybee.shoprotation.admin")) {
                    main.messages.get("no-perms").send(player)
                    CustomSound.ERROR.playTo(player)
                    return@setItem
                }
                CustomSound.CLICK.playTo(player)
                UtilsRewards.openGUIRewards(player, GUIView.REWARDS, args)
            }

            .setItem(6, 5, ItemBuilder.of(Material.OAK_SIGN)
                .name("&eHilfe!")
                .description("&7Zeigt eine Hilfe an!")
                .asItem()) {
                sendHelp(player)
                player.closeInventory()
            }

            .setItem(6,6, ItemBuilder.of(Material.WHITE_SHULKER_BOX)
                .name("&eZiele!")
                .description("&6Zeigt alle Ziele an, welche erreicht werden können!")
                .asItem()) {
                if(!player.hasPermission("skybee.shoprotation.admin")) {
                    main.messages.get("no-perms").send(player)
                    CustomSound.ERROR.playTo(player)
                    return@setItem
                }
                CustomSound.CLICK.playTo(player)
                UtilsChestItems.openGUIChestItems(player, GUIView.LOOTPOOL, args)
            }
    }

    fun setPresetBorder(player: Player, gui: GUI, material: Material) {
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