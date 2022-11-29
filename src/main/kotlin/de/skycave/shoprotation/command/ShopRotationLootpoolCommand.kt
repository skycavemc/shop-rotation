package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.display.GUIView
import de.skycave.shoprotation.utils.UtilsChestItems
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ShopRotationLootpoolCommand: java.util.function.BiFunction<CommandSender, Array<out String>, Boolean> {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    override fun apply(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            main.messages.get("no-player").send(sender)
            return true
        }
        when (args[1].lowercase()) {
            "addhanditem" -> {
                if(!sender.hasPermission("skybee.shoprotation.lootpool.add")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                val handitem = sender.inventory.itemInMainHand

                val name = args[2].lowercase()
                val filter = Filters.eq("name", name)
                val lootpool = main.chestItems.find(filter).first()

                if(lootpool != null) {
                    if(handitem.type != Material.AIR) {
                        lootpool.items[handitem.type] = handitem.amount
                        main.chestItems.replaceOne(Filters.eq("name", name), lootpool)
                        main.messages.get("add-item-to-lootpool-success")
                            .replace("%material", handitem.type.name)
                            .replace("%amount", handitem.amount.toString())
                            .send(sender)
                    }
                    return true
                } else {
                    main.messages.get("load-lootpool-error").send(sender)
                    return true
                }
            }
            "add" -> {
                if(!sender.hasPermission("skybee.shoprotation.lootpool.add")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                val name = args[2].lowercase()

                val materialtoCheck = args[3].lowercase()
                val amount = args[4].lowercase()

                if(!isNumeric(amount)) {
                    main.messages.get("invalid-number").send(sender)
                    return true
                }
                if(!isMaterial(materialtoCheck)) {
                    main.messages.get("invalid-material").send(sender)
                    return true
                }
                val material = Material.getMaterial(args[3].lowercase())
                if(material == Material.AIR) {
                    main.messages.get("invalid-material").send(sender)
                    return true
                }
                val filter = Filters.eq("name", name)
                val lootpool = main.chestItems.find(filter).first()

                if(lootpool != null) {
                    if(lootpool != null) {
                        lootpool.items[material] = amount.toInt()
                    }
                    main.chestItems.replaceOne(Filters.eq("name", name), lootpool)
                    main.messages.get("add-item-to-lootpool-success")
                        .replace("%material", material.toString())
                        .replace("%amount", amount)
                        .send(sender)
                    return true
                }
            }
            "remove" -> {
                if(!sender.hasPermission("skybee.shoprotation.lootpool.remove")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                if(args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                UtilsChestItems.openGUIChestItems(sender, GUIView.LOOTPOOL_REMOVE, args)
                return true
            }
            "show" -> {
                if(!sender.hasPermission("skybee.shoprotation.lootpool.show")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                if(args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                UtilsChestItems.openGUIChestItems(sender, GUIView.LOOTPOOL, args)
                return true
            }
            else -> {
                main.messages.get("message-unknown").send(sender)
                return true
            }
        }
        return true
    }

    private fun isNumeric(toCheck: String): Boolean {
        val regex = "[0-9]".toRegex()
        return toCheck.matches(regex)
    }

    private fun isMaterial(toCheck: String): Boolean {
        for(material in Material.values()) {
            if(material.toString() == toCheck) {
                return true
            }
        }
        return false
    }
}