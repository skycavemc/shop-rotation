package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.Chest
import de.skycave.shoprotation.model.display.GUIView
import de.skycave.shoprotation.utils.Formatting
import de.skycave.shoprotation.utils.Utils
import org.bukkit.Material
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
            "chest" -> {
                if(args.size < 2) {
                    sendHelp(sender)
                    return true
                }
                return ShopRotationSubCommand().apply(sender, args)
            }
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

                    main.chests.insertOne(chest)
                    main.messages.get("chest-created-success")
                        .replace("%name", name)
                        .replace("%location", Formatting.formatLocation(player.location))
                        .send(sender)
                    return true
                }
                main.chests.updateOne(Filters.eq("name", name), Updates.set("location", player.location))
                main.messages.get("set-location-success")
                    .replace("%name", name)
                    .replace("%location", Formatting.formatLocation(player.location))
                    .send(sender)
                return true
            }
            "opengui" -> {
                if(sender is Player) {
                    Utils.openGUI(sender, GUIView.MAIN)
                }
            }
            "remove" -> {

            }
            "current" -> {

            }
            "lootpool" -> {
                if(args.size < 2) {
                    sendHelp(sender)
                    return true
                }
                return ShopRotationLootpoolCommand().apply(sender, args)
            }
            "rewards" -> {
                if(args.size < 2) {
                    sendHelp(sender)
                    return true
                }
                return ShopRotationRewardsCommand().apply(sender, args)
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
                    main.chests.updateMany(Filters.exists("name"), Updates.set("enabled", true))
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
                    main.chests.updateOne(Filters.eq("name", name), Updates.set("enabled", true))
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
                    main.chests.updateMany(Filters.exists("name"), Updates.set("enabled", false))
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
                    main.chests.updateOne(Filters.eq("name", name), Updates.set("enabled", false))
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
        //TODO:Add missing messages to help command
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        var arguments = emptyList<String>()
        val completions = ArrayList<String>()

        if(args.size == 1) {
            arguments = listOf("setlocation", "help", "remove", "items", "enable", "disable", "opengui", "chest", "lootpool", "rewards")
            StringUtil.copyPartialMatches(args[0], arguments, completions)
        }
        if(args.size == 2) {
            when (args[0].lowercase()) {
                "chest" -> {
                    arguments = listOf("get", "getall", "current")
                    //TODO: WHAT THE F DID I DO HERE?? CHECK IT MF! @ME
                }
                "remove" -> {
                    val chests = main.chests.find()
                    arguments = chests.map { it.name }.toList()
                }
                "lootpool" -> {
                    arguments = listOf("addhanditem", "add", "remove", "show")
                }
                "rewards" -> {
                    arguments = listOf("addhanditem", "add", "remove", "show")
                }
            }
            StringUtil.copyPartialMatches(args[1], arguments, completions)
        }
        if(args.size == 4) {
            if(args[1].lowercase() == "add" && (args[0].lowercase() == "lootpool" || args[0].lowercase() == "rewards")) {
                val materiallist = emptyList<String>()
                for(material in Material.values()) {
                    materiallist.toMutableList().add(material.toString())
                }
                arguments = materiallist
            }
            StringUtil.copyPartialMatches(args[3], arguments, completions)
        }
        completions.sort()
        return completions
    }
}