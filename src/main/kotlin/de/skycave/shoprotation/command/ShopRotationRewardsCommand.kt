package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.Rewards
import de.skycave.shoprotation.model.display.GUIView
import de.skycave.shoprotation.utils.UtilsRewards
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ShopRotationRewardsCommand : java.util.function.BiFunction<CommandSender, Array<out String>, Boolean> {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    override fun apply(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            main.messages.get("no-player").send(sender)
            return true
        }
        when (args[1].lowercase()) {
            "addhanditem" -> {
                //shoprotation rewards addhanditem <name>
                if (args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                val handitem = sender.inventory.itemInMainHand

                if (handitem.type == Material.AIR) {
                    main.messages.get("material-air-not-allowed").send(sender)
                    return true
                }

                val name = args[2].lowercase()
                val filter = Filters.eq("name", name)
                var rewards = main.rewards.find(filter).first()

                if (rewards == null) {
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
                //shoprotation rewards add <material> <amount> <name>
                if (args.size < 5) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }

                val materialtoCheck = args[2].lowercase()
                if (!isMaterial(materialtoCheck)) {
                    main.messages.get("invalid-material").send(sender)
                    return true
                }
                val material = Material.getMaterial(args[2].lowercase())
                val amount = args[3].lowercase()
                val name = args[4].lowercase()
                println("$materialtoCheck, $material, $amount, $name")

                if (!isNumeric(amount)) {
                    main.messages.get("invalid-number").send(sender)
                    return true
                }
                if (material == Material.AIR) {
                    main.messages.get("material-air-not-allowed").send(sender)
                    return true
                }
                val filter = Filters.eq("name", name)
                val rewards = main.rewards.find(filter).first()

                if (rewards != null) {
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
                //shoprotation rewards remove <name>
                if (args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                val name = args[2]
                UtilsRewards.openGUIRewards(sender, GUIView.REWARDS_REMOVE, name)
                return true
            }

            "show" -> {
                //shoprotation rewards show <name>
                if (args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                val name = args[2]
                UtilsRewards.openGUIRewards(sender, GUIView.REWARDS, name)
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

    private fun isMaterial(toCheck: String) = Material.values()
        .map { it.toString() }
        .contains(toCheck)
}