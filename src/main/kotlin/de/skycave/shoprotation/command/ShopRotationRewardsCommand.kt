package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.display.GUIView
import de.skycave.shoprotation.utils.Utils
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ShopRotationRewardsCommand: java.util.function.BiFunction<CommandSender, Array<out String>, Boolean>  {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    override fun apply(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            main.messages.get("no-player").send(sender)
            return true
        }
        when (args[1].lowercase()) {
            "addhanditem" -> {
                if(!sender.hasPermission("skybee.shoprotation.rewards.add")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                val handitem = sender.inventory.itemInMainHand

                val name = args[2].lowercase()
                val filter = Filters.eq("name", name)
                val rewards = main.rewards.find(filter).first()

                if(rewards != null) {
                    if (handitem.type != Material.AIR) {

                        rewards.rewardlist[handitem.type] = handitem.amount

                        main.rewards.replaceOne(Filters.eq("name", name), rewards)
                        main.messages.get("add-item-to-rewards-success")
                            .replace("%material", handitem.type.name)
                            .replace("%amount", handitem.amount.toString())
                            .send(sender)
                    }
                    return true
                } else {
                    main.messages.get("load-rewards-error").send(sender)
                    return true
                }
            }
            "add" -> {
                if(!sender.hasPermission("skybee.shoprotation.rewards.add")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                val name = args[2].lowercase()

                val material = Material.getMaterial(args[3].lowercase())
                val amount = args[4].lowercase()

                if(!isNumeric(amount)) {
                    main.messages.get("invalid-number").send(sender)
                    return true
                }
                if(material == Material.AIR) {
                    main.messages.get("invalid-material").send(sender)
                    return true
                }
                val filter = Filters.eq("name", name)
                val rewards = main.rewards.find(filter).first()

                if(rewards != null) {
                    if (material != null) {
                        rewards.rewardlist[material] = amount.toInt()
                    }
                    main.rewards.replaceOne(Filters.eq("name", name), rewards)
                    main.messages.get("add-item-to-rewards-success")
                        .replace("%material", material.toString())
                        .replace("%amount", amount)
                        .send(sender)
                    return true
                }
            }
            "remove" -> {
                if(!sender.hasPermission("skybee.shoprotation.rewards.remove")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                if(args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                Utils.openGUIRewards(sender, GUIView.REWARDS_REMOVE, args)
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
                Utils.openGUIRewards(sender, GUIView.REWARDS, args)
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
}