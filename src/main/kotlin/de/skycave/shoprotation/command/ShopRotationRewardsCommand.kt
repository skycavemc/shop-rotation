package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.Rewards
import de.skycave.shoprotation.model.display.GUIView
import de.skycave.shoprotation.utils.Utils
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
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
                    return true
                }
                val handitem = sender.inventory.itemInMainHand

                val name = args[2].lowercase()
                val filter = Filters.eq("name", name)
                var rewards = main.rewards.find(filter).first()

                if(rewards != null) {
                    rewards = Rewards()
                    rewards.name = name

                    //Create New HashMap to add handitem
                    val rewardHashMap = HashMap<Material, Int>()

                    if (handitem.type != Material.AIR) {
                        rewardHashMap[handitem.type] = handitem.amount

                        //Add HashMap to Reward DB:
                        rewards.rewardlist = rewardHashMap
                        //Safe rewardlist in rewards db
                        main.rewards.updateOne(Filters.eq("name", name), Updates.set("rewardlist", rewards.rewardlist))
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
                var rewards = main.rewards.find(filter).first()

                if(rewards != null) {
                    rewards = Rewards()
                    rewards.name = name

                    val rewardHashMap = HashMap<Material, Int>()
                    if (material != null) {
                        rewardHashMap[material] = amount.toInt()
                    }
                    rewards.rewardlist = rewardHashMap

                    main.rewards.updateOne(Filters.eq("name", name), Updates.set("rewardlist", rewards.rewardlist))
                    main.messages.get("add-item-to-rewards-success")
                        .replace("%material", material.toString())
                        .replace("%amount", amount)
                        .send(sender)
                    return true
                }
            }
            "remove" -> {

            }
            "show" -> {
                if(!sender.hasPermission("skybee.shoprotation.rewards.show")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                Utils.openGUI(sender, GUIView.REWARDS)
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