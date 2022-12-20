package de.skycave.shoprotation.command

import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.enums.Message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.BiFunction

class ShopRotationSubCommand : BiFunction<CommandSender, Array<out String>, Boolean> {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    override fun apply(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            Message.NO_PLAYER.get().send(sender)
            return true
        }

        when (args[1].lowercase()) {
            "get" -> {

            }

            "getall" -> {

            }

            "current" -> {
                val chest = main.chests.find().first()
                val currentitem = chest?.item.toString()
                val requiredamount = chest?.requiredAmount.toString()
                val amount = chest?.amount.toString()

                sender.sendMessage("&eCurrent item: &f %currentitem &7(&f%amount&7/&f%requiredamount&7)")
                Message.CHEST_CURRENT_ITEM.get()
                    .replace("%currentitem", currentitem)
                    .replace("%amount", amount)
                    .replace("%requiredamount", requiredamount)
                    .send(sender)
                return true
            }

            else -> {
                Message.MESSAGE_UNKNOWN.get().send(sender)
                return true
            }
        }
        return true
    }
}