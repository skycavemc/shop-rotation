package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.Rewards
import de.skycave.shoprotation.model.display.GUIView
import de.skycave.shoprotation.utils.Utils
import de.skycave.shoprotation.utils.UtilsRewards
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.ArrayList

class ShopRotationRewardsCommand: java.util.function.BiFunction<CommandSender, Array<out String>, Boolean>  {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    override fun apply(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            main.messages.get("no-player").send(sender)
            return true
        }
        when (args[1].lowercase()) {
            "addhanditem" -> {
                if(!sender.hasPermission("skybee.shoprotation.admin")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                val handitem = sender.inventory.itemInMainHand

                if (handitem.type == Material.AIR) {
                    main.messages.get("material-air-not-allowed").send(sender)
                    return true
                }

                if(args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }

                val name = args[2].lowercase()
                val filter = Filters.eq("name", name)
                var rewards = main.rewards.find(filter).first()

                if(rewards == null) {
                    rewards = Rewards()
                    rewards.name = name
                    rewards.rewardlist = EnumMap(Material::class.java)
                    main.rewards.insertOne(rewards)
                }

                rewards.rewardlist[handitem.type] = handitem.amount
                main.rewards.replaceOne(Filters.eq("name", name), rewards)
                main.messages.get("add-item-to-rewards-success")
                    .replace("%material", handitem.type.name)
                    .replace("%amount", handitem.amount.toString())
                    .send(sender)

            }
            "add" -> {
                if(!sender.hasPermission("skybee.shoprotation.admin")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                val name = args[2].lowercase()

                val materialtoCheck = args[3].lowercase()
                if(!isMaterial(materialtoCheck)) {
                    main.messages.get("invalid-material").send(sender)
                    return true
                }
                val material = Material.getMaterial(args[3].lowercase())
                val amount = args[4].lowercase()

                if(!isNumeric(amount)) {
                    main.messages.get("invalid-number").send(sender)
                    return true
                }
                if(material == Material.AIR) {
                    main.messages.get("material-air-not-allowed").send(sender)
                    return true
                }
                val filter = Filters.eq("name", name)
                var rewards = main.rewards.find(filter).first()

                if(rewards == null) {
                    rewards = Rewards()
                    rewards.name = name
                    rewards.rewardlist = EnumMap(Material::class.java)
                    main.rewards.insertOne(rewards)
                }
                rewards.rewardlist[material] = amount.toInt()

                main.rewards.replaceOne(Filters.eq("name", name), rewards)
                main.messages.get("add-item-to-rewards-success")
                    .replace("%material", material.toString())
                    .replace("%amount", amount)
                    .send(sender)
            }
            "remove" -> {
                if(!sender.hasPermission("skybee.shoprotation.admin")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                if(args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                UtilsRewards.openGUIRewards(sender, GUIView.REWARDS_REMOVE, args)
                return true
            }
            "show" -> {
                if(!sender.hasPermission("skybee.shoprotation.rewards.show")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                if(args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                UtilsRewards.openGUIRewards(sender, GUIView.REWARDS, args)
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