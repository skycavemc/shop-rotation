package de.skycave.shoprotation.utils

import com.mongodb.client.model.Filters
import de.leonheuer.mcguiapi.gui.GUIPattern
import de.leonheuer.mcguiapi.utils.ItemBuilder
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.display.CustomSound
import de.skycave.shoprotation.model.display.GUIView
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.plugin.java.JavaPlugin

object Utils {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun openGUI(player: Player, view: GUIView) {
        val gui = main.guiFactory.createGUI(6, view.getTitle())

        if(view != GUIView.MAIN) {
            setPresetItems(player, view)
        }

        if(view == GUIView.MAIN) {

        }
        gui.show(player)
    }

    fun openGUIRewards(player: Player, view: GUIView, args: Array<out String>) {
        when (view) {
            GUIView.REWARDS -> {
                val name = args[2]

                val gui = main.guiFactory.createGUI(6, view.getTitle())
                setPresetItems(player, view)

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
                            .asItem()
                        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                        gui.setItem(slot, item)
                    }
                }
                gui.show(player)
                return
            }
            GUIView.REWARDS_REMOVE -> {
                val name = args[2]

                val gui = main.guiFactory.createGUI(6, view.getTitle())
                setPresetItems(player, view)

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

    fun openGUILootpool(player: Player, view: GUIView, args: Array<out String>) {
        when (view) {
            GUIView.LOOTPOOL -> {
                val name = args[2]

                val gui = main.guiFactory.createGUI(6, view.getTitle())
                setPresetItems(player, view)

                val filter = Filters.eq("name", name)
                val chestItems = main.chestItems.find(filter).first()
                if(chestItems != null) {
                    val chestitemslist = chestItems.items.entries
                    var slot = 9
                    for((material, amount) in chestitemslist) {
                        if(slot.mod(9) == 0) {
                            slot++
                        } else if((slot + 1).mod(9) == 0) {
                            slot += 2
                        }
                        var amountfinal = amount
                        if(amount > 64) {
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
                val name = args[2]

                val gui = main.guiFactory.createGUI(6, view.getTitle())
                setPresetItems(player, view)

                val filter = Filters.eq("name", name)
                val chestItems = main.chestItems.find(filter).first()
                if(chestItems != null) {
                    val chestitemslist = chestItems.items.entries
                    var slot = 9
                    for((material, amount) in chestitemslist) {
                        if(slot.mod(9) == 0) {
                            slot++
                        } else if((slot + 1).mod(9) == 0) {
                            slot += 2
                        }
                        var amountfinal = amount
                        if(amount > 64) {
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
                            openGUILootpool(player, view, args)
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

    private fun setPresetItems(player: Player, view: GUIView) {
        val gui = main.guiFactory.createGUI(6, view.getTitle())

        val pattern = GUIPattern.ofPattern("bbbbbbbbb")
            .withMaterial('b', ItemBuilder.of(Material.CYAN_STAINED_GLASS_PANE).name("§0").asItem())
        gui.formatPattern(pattern.startAtLine(1)).formatPattern(pattern.startAtLine(6))
            .setItem(6,1, ItemBuilder.of(Material.ARROW).name("&cZurück").asItem()) {
                CustomSound.CLICK.playTo(player)
                openGUI(player, GUIView.MAIN)
            }
            .setItem(6,4, ItemBuilder.of(Material.NETHER_STAR)
                .name("&dBelohnungen")
                .description("&7Zeigt alle möglichen Belohnungen an!")
                .asItem()) {
                if(!player.hasPermission("skybee.shoprotation.rewards.show")) {
                    main.messages.get("no-perms").send(player)
                    CustomSound.ERROR.playTo(player)
                    return@setItem
                }
                CustomSound.CLICK.playTo(player)
                openGUI(player, GUIView.REWARDS)
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
                .description("&7Zeigt alle Ziele an, welche erreicht werden können!")
                .asItem()) {
                if(!player.hasPermission("skybee.shoprotation.lootpool.show")) {
                    main.messages.get("no-perms").send(player)
                    CustomSound.ERROR.playTo(player)
                    return@setItem
                }
                CustomSound.CLICK.playTo(player)
                openGUI(player, GUIView.LOOTPOOL)
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