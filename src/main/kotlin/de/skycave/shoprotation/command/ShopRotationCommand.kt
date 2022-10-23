package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.Chest
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
            sendHelp(sender)
            return true
        }

        when (args[0].lowercase()) {
            "setlocation" -> {
                if(!checkConditions(true, "skybee.shoprotation.admin", sender)) {
                    return true
                }
                val player = sender as Player
                if(args.size < 2){
                    main.messages.get("set-location-syntax").send(sender)
                    return true
                }
                val name = args[1].lowercase()
                val filter = Filters.eq("name", name)
                var chest = main.chests.find(filter).first()

                if(chest == null) {
                    chest = Chest()
                    chest.name = name
                    chest.location = player.location
                }
            }
            "open" -> {

            }
            "delete" -> {

            }
            "current" -> {

            }
            "items" -> {

            }
            "enable" -> {
                if(!checkConditions(false, "skybee.shoprotation.enable", sender)) {
                    return true
                }
                if(args.size < 2) {
                    main.messages.get("set-enabled-syntax").send(sender)
                    return true
                }
                val name = args[1].lowercase()
                if(name == "all") {
                    main.chests.find().forEach {
                        it.enabled = true
                    }
                    main.messages.get("enabled-all").send(sender)
                    return true
                }

                val filter = Filters.eq("name", name)
                val chest = main.chests.find(filter).first()

                if(chest == null) {
                    main.messages.get("chest-unknown").replace("%name", name).send(sender)
                    return true
                }
                if(!chest.enabled) {
                    chest.enabled = true
                    main.messages.get("set-enabled-success").send(sender)
                } else {
                    main.messages.get("already-enabled").send(sender)
                }
                return true
            }
            "disable" -> {
                if(!checkConditions(false, "skybee.shoprotation.disable", sender)) {
                    return true
                }
                if(args.size < 2) {
                    main.messages.get("set-disabled-syntax").send(sender)
                    return true
                }
                val name = args[1].lowercase()
                if(name == "all") {
                    main.chests.find().forEach {
                        it.enabled = false
                    }
                    main.messages.get("disabled-all").send(sender)
                    return true
                }

                val filter = Filters.eq("name", name)
                val chest = main.chests.find(filter).first()

                if(chest == null) {
                    main.messages.get("chest-unknown").replace("%name", name).send(sender)
                    return true
                }
                if(chest.enabled) {
                    chest.enabled = false
                    main.messages.get("set-disabled-success").send(sender)
                } else {
                    main.messages.get("already-disabled").send(sender)
                }
                return true
            }
            "help" -> sendHelp(sender)
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

    private fun sendHelp(sender: CommandSender) {
        main.messages.get("chest-set-location").send(sender)
        main.messages.get("chest-open-gui").send(sender)
        main.messages.get("chest-delete-items").send(sender)
        main.messages.get("chest-show-items").send(sender)
        main.messages.get("chest-show-current-item").send(sender)
        main.messages.get("chest-enable").send(sender)
        main.messages.get("chest-disable").send(sender)
        main.messages.get("chest-help").send(sender)

    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val arguments = ArrayList<String>()
        val completions = ArrayList<String>()

        if (args.size == 1) {
            arguments.addAll(arrayOf(
                "setlocation", "open", "help", "delete", "current", "items", "enable", "disable")
            )
            StringUtil.copyPartialMatches(args[0], arguments, completions)
        }

        completions.sort()
        return completions
    }


}