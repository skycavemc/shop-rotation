package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.enums.Message
import de.skycave.shoprotation.model.ChestItems
import de.skycave.shoprotation.model.display.GUIView
import de.skycave.shoprotation.utils.UtilsChestItems
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ShopRotationLootpoolCommand : java.util.function.BiFunction<CommandSender, Array<out String>, Boolean> {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    override fun apply(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            Message.NO_PLAYER.get().send(sender)
            return true
        }
        when (args[1].lowercase()) {
            "addhanditem" -> {
                //shoprotation lootpool addhanditem <name>
                if (args.size < 3) {
                    Message.NOT_ENOUGH_ARGS.get().send(sender)
                    return true
                }
                val handitem = sender.inventory.itemInMainHand

                if (handitem.type == Material.AIR) {
                    Message.MATERIAL_AIR_NOT_ALLOWED.get().send(sender)
                    return true
                }

                val name = args[2].lowercase()
                val filter = Filters.eq("name", name)
                var lootpool = main.chestItems.find(filter).first()

                if (lootpool == null) {
                    lootpool = ChestItems()
                    lootpool.name = name
                    lootpool.items = EnumMap(Material::class.java)
                    main.chestItems.insertOne(lootpool)
                }

                lootpool.items[handitem.type] = handitem.amount
                main.chestItems.replaceOne(Filters.eq("name", name), lootpool)
                Message.ADD_ITEM_TO_LOOTPOOL.get()
                    .replace("%material", handitem.type.name)
                    .replace("%amount", handitem.amount.toString())
                    .send(sender)
            }

            "add" -> {
                //shoprotation lootpool add <material> <amount> <name>
                if (args.size < 5) {
                    Message.NOT_ENOUGH_ARGS.get().send(sender)
                    return true
                }
                val materialtoCheck = args[2].lowercase()
                if (!isMaterial(materialtoCheck)) {
                    Message.INVALID_MATERIAL.get().send(sender)
                    return true
                }
                val material = Material.getMaterial(args[2].lowercase())
                val amount = args[3].lowercase()
                val name = args[4].lowercase()
                if (!isNumeric(amount)) {
                    Message.INVALID_NUMBER.get().send(sender)
                    return true
                }
                if (material == Material.AIR) {
                    Message.MATERIAL_AIR_NOT_ALLOWED.get().send(sender)
                    return true
                }
                val filter = Filters.eq("name", name)
                val lootpool = main.chestItems.find(filter).first()

                if (lootpool != null) {
                    if (material != null) {
                        lootpool.items[material] = amount.toInt()
                    }
                    main.chestItems.replaceOne(Filters.eq("name", name), lootpool)
                    Message.ADD_ITEM_TO_LOOTPOOL.get()
                        .replace("%material", material.toString())
                        .replace("%amount", amount)
                        .send(sender)
                }
            }

            "remove" -> {
                //shoprotation lootpool remove <name>
                if (args.size < 3) {
                    Message.NOT_ENOUGH_ARGS.get().send(sender)
                    return true
                }
                val name = args[2]
                UtilsChestItems.openGUIChestItems(sender, GUIView.LOOTPOOL_REMOVE, name)
                return true
            }

            "show" -> {
                //shoprotation lootpool show <name>
                if (args.size < 3) {
                    Message.NOT_ENOUGH_ARGS.get().send(sender)
                    return true
                }
                val name = args[2]
                UtilsChestItems.openGUIChestItems(sender, GUIView.LOOTPOOL, name)
                return true
            }

            else -> {
                Message.MESSAGE_UNKNOWN.get().send(sender)
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
        for (material in Material.values()) {
            if (material.toString() == toCheck) {
                return true
            }
        }
        return false
    }
}