package de.skycave.shoprotation.command

import de.skycave.shoprotation.Enums.Message
import de.skycave.shoprotation.ShopRotation
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class ShopRotationCommand(private val main: ShopRotation): CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        TODO("Not yet implemented")
    }

    @Suppress("SameParameterValue")
    private fun checkConditions(playeronly: Boolean, permission: String?, sender: CommandSender) : Boolean {
        if(playeronly) {
            if(sender !is Player) {
                sender.sendMessage(Message.NO_PLAYER.getMessage())
                return sender is Player
            }
            if(permission != null && !sender.hasPermission(permission)) {
                sender.sendMessage(Message.NO_PERMS.getMessage())
                return false
            }
        }
        return true
    }



    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val arguments = ArrayList<String>()
        val completions = ArrayList<String>()

        if (args.size == 1) {
            arguments.addAll(arrayOf(
                "setlocation", "close", "open", "info", "help", "reset") //TODO: Finalize all necessary Commands
            )
            StringUtil.copyPartialMatches(args[0], arguments, completions)
        }

        completions.sort()
        return completions
    }


}