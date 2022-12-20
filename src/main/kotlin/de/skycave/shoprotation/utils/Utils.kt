package de.skycave.shoprotation.utils

import de.leonheuer.mcguiapi.gui.GUI
import de.leonheuer.mcguiapi.gui.GUIPattern
import de.leonheuer.mcguiapi.utils.ItemBuilder
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.enums.Message
import de.skycave.shoprotation.model.display.CustomSound
import de.skycave.shoprotation.model.display.GUIView
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object Utils {
    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun openGUIMain(player: Player, name: String) {
        val view = GUIView.MAIN
        val gui = main.guiFactory.createGUI(6, view.getTitle())
        println("dev122")
        setPresetMain(player, gui, Material.LIGHT_BLUE_STAINED_GLASS_PANE, name)
        println("dev12")
        gui.show(player)
    }

    fun setPresetBorder(gui: GUI, material: Material) {
        val pattern = GUIPattern.ofPattern("bbbbbbbbb")
            .withMaterial('b', ItemBuilder.of(material).name("§0").asItem())
        gui.formatPattern(pattern.startAtLine(1))
            .formatPattern(pattern.startAtLine(6))
        var slot = 9
        while (slot < 54) {
            if (slot.mod(9) == 0) {
                gui.setItem(slot, ItemBuilder.of(material).name("§0").asItem())
            } else if (slot == 17 || slot == 26 || slot == 35 || slot == 44) {
                gui.setItem(slot, ItemBuilder.of(material).name("§0").asItem())
            }
            slot++
        }
    }

    private fun setPresetMain(player: Player, gui: GUI, material: Material, name: String) {
        val pattern = GUIPattern.ofPattern("bbbbbbbbb")
            .withMaterial('b', ItemBuilder.of(material).name("§0").asItem())
        gui.formatPattern(pattern.startAtLine(1))
            .formatPattern(pattern.startAtLine(6))

        val patterngray = GUIPattern.ofPattern("bbbbbbbbb")
            .withMaterial('b', ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name("§0").asItem())
        gui.formatPattern(patterngray.startAtLine(2))
            .formatPattern(patterngray.startAtLine(3))
            .formatPattern(patterngray.startAtLine(5))

            .setItem(
                6, 4, ItemBuilder.of(Material.NETHER_STAR)
                    .name("&dBelohnungen")
                    .description("&7Zeigt alle möglichen Belohnungen an!")
                    .asItem()
            ) {
                if (!player.hasPermission("skybee.shoprotation.admin")) {
                    Message.NO_PERMS.get().send(player)
                    CustomSound.ERROR.playTo(player)
                    return@setItem
                }
                CustomSound.CLICK.playTo(player)
                UtilsRewards.openGUIRewards(player, GUIView.REWARDS, name)
            }

            .setItem(
                6, 5, ItemBuilder.of(Material.OAK_SIGN)
                    .name("&eHilfe!")
                    .description("&7Zeigt eine Hilfe an!")
                    .asItem()
            ) {
                Help.sendHelp(player)
                player.closeInventory()
            }

            .setItem(
                6, 6, ItemBuilder.of(Material.WHITE_SHULKER_BOX)
                    .name("&eZiele!")
                    .description("&6Zeigt alle Ziele an, welche erreicht werden können!")
                    .asItem()
            ) {
                if (!player.hasPermission("skybee.shoprotation.admin")) {
                    Message.NO_PERMS.get().send(player)
                    CustomSound.ERROR.playTo(player)
                    return@setItem
                }
                CustomSound.CLICK.playTo(player)
                UtilsChestItems.openGUIChestItems(player, GUIView.LOOTPOOL, name)
            }
    }
}