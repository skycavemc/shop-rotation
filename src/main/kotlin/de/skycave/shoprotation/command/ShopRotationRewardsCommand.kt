package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.Rewards
import de.skycave.shoprotation.model.display.GUIView
import de.skycave.shoprotation.utils.Utils
import jdk.jshell.execution.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
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
                val handitem = sender.mainHand

                val name = args[2].lowercase()
                val filter = Filters.eq("name", name)
                var rewards = main.rewards.find(filter).first()

                if(rewards != null) {
                    rewards = Rewards()
                    rewards.name = name
                    val rewardlist = rewards.rewardlist


                }

            }
            "add" -> {

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
}