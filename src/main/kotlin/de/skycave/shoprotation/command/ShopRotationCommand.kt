package de.skycave.shoprotation.command

import de.skycave.shoprotation.ShopRotation
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import kotlin.collections.ArrayList

class ShopRotationCommand(private val main: ShopRotation): CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(args.isEmpty()) {
            help(sender)
            return true
        }

        when (args[0].lowercase()) {
            "setlocation" -> {
                if(!checkConditions(true, "skybee.shoprotation.admin", sender)) {
                    return true
                }



            }
            "close" -> {

            }
            "open" -> {

            }
            "info" -> {

            }
            "help" -> {

            }
            "reset" -> {

            }
            "reloadconfig" -> {

            }
            else -> {
                main.messages.get("message-unknown").send(sender)
                return true
            }
        }

        return true
    }

    @Suppress("SameParameterValue")
    private fun checkConditions(playeronly: Boolean, permission: String?, sender: CommandSender) : Boolean {
        if(playeronly) {
            if(sender !is Player) {
                main.messages.get("no-player").send(sender)
                return sender is Player
            }
            if(permission != null && !sender.hasPermission(permission)) {
                main.messages.get("no-perms").send(sender)
                return false
            }
        }
        return true
    }

    private fun help(sender: CommandSender) {
        TODO("Implement Messages in Enum")
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val arguments = ArrayList<String>()
        val completions = ArrayList<String>()

        if (args.size == 1) {
            arguments.addAll(arrayOf(
                "setlocation", "close", "open", "info", "help", "reset", "reloadconfig") //TODO: Finalize all necessary Commands
            )
            StringUtil.copyPartialMatches(args[0], arguments, completions)
        }

        completions.sort()
        return completions
    }


}