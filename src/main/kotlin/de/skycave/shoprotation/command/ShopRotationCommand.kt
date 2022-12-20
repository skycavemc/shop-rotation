package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.enums.Message
import de.skycave.shoprotation.model.Chest
import de.skycave.shoprotation.utils.Formatting
import de.skycave.shoprotation.utils.Help
import de.skycave.shoprotation.utils.Utils
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class ShopRotationCommand(private val main: ShopRotation) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            Message.NO_PLAYER.get().send(sender)
            return true
        }
        if (!sender.hasPermission("skybee.shoprotation.admin")) {
            Message.NO_PERMS.get().send(sender)
            return true
        }
        if (args.isEmpty()) {
            Help.sendHelp(sender)
            return true
        }
        when (args[0].lowercase()) {
            "chest" -> {
                if (args.size < 2) {
                    Help.sendHelp(sender)
                    return true
                }
                return ShopRotationSubCommand().apply(sender, args)
            }

            "setlocation" -> {
                //shoprotation setlocation <name>
                if (args.size < 2) {
                    Message.LOCATION_SYNTAX.get().send(sender)
                    return true
                }
                val name = args[1].lowercase()
                val filter = Filters.eq("name", name)
                var chest = main.chests.find(filter).first()

                val block = sender.getTargetBlock(3)

                if (block == null) {
                    Message.LOOK_AT_CHEST.get().send(sender)
                    return true
                }

                if (block.type != Material.CHEST) {
                    Message.LOOK_AT_CHEST.get().send(sender)
                    return true
                }
                if (chest == null) {
                    chest = Chest()
                    chest.name = name
                    chest.location = block.location
                    main.chests.insertOne(chest)
                    Message.CHEST_CREATED_SUCESSUL.get()
                        .replace("%name", name)
                        .replace("%location", Formatting.formatLocation(block.location))
                        .send(sender)
                    return true
                }
                main.chests.updateOne(Filters.eq("name", name), Updates.set("location", block.location))
                Message.LOCATION_SET_SUCESS.get()
                    .replace("%name", name)
                    .replace("%location", Formatting.formatLocation(block.location))
                    .send(sender)
                return true
            }

            "opengui" -> {
                //shoprotation opengui <name>
                if (args.size < 2) {
                    Message.NOT_ENOUGH_ARGS.get().send(sender)
                    return true
                }
                val name = args[1]
                Utils.openGUIMain(sender, name)
            }

            "remove" -> {
                //shoprotation remove <name>
                if (args.size < 2) {
                    Message.NOT_ENOUGH_ARGS.get().send(sender)
                }
                val name = args[1]
                val filter = Filters.eq("name", name)
                val chest = main.chests.find(filter).first()
                if (chest == null) {
                    Message.COULD_NOT_FIND_CHEST.get().send(sender)
                    return true;
                }
                main.chests.deleteOne(filter)
                Message.CHEST_REMOVE_SUCESS.get()
                    .replace("%name", name)
                    .send(sender)
            }

            "lootpool" -> {
                //SubCommand
                if (args.size < 2) {
                    Help.sendHelp(sender)
                    return true
                }
                if (!sender.hasPermission("skybee.shoprotation.admin")) {
                    Message.NO_PERMS.get().send(sender)
                    return true
                }
                return ShopRotationLootpoolCommand().apply(sender, args)
            }

            "enable" -> {
                //shoprotation enable <name>
                if (args.size < 2) {
                    Message.CHEST_ENABLE.get().send(sender)
                    return true
                }
                val name = args[1].lowercase()
                if (name == "all") {
                    main.chests.updateMany(Filters.exists("name"), Updates.set("enabled", true))
                    Message.CHEST_ENABLED_ALL.get().send(sender)
                    return true
                }

                val filter = Filters.eq("name", name)
                val chest = main.chests.find(filter).first()

                if (chest == null) {
                    Message.CHEST_UNKNOWN.get()
                        .replace("%name", name)
                        .send(sender)
                    return true
                }
                if (!chest.enabled) {
                    main.chests.updateOne(Filters.eq("name", name), Updates.set("enabled", true))
                    Message.CHEST_ENABLED_SUCESS.get().send(sender)
                } else {
                    Message.CHEST_ALREADY_ENABLED.get().send(sender)
                }
                return true
            }

            "disable" -> {
                //shoprotation disable <name>
                if (args.size < 2) {
                    Message.CHEST_DISABLE.get().send(sender)
                    return true
                }
                val name = args[1].lowercase()
                if (name == "all") {
                    main.chests.updateMany(Filters.exists("name"), Updates.set("enabled", false))
                    Message.CHEST_DISABLED_ALL.get().send(sender)
                    return true
                }

                val filter = Filters.eq("name", name)
                val chest = main.chests.find(filter).first()

                if (chest == null) {
                    Message.CHEST_UNKNOWN.get().send(sender)
                    return true
                }
                if (chest.enabled) {
                    main.chests.updateOne(Filters.eq("name", name), Updates.set("enabled", false))
                    Message.CHEST_DISABLED_SUCESS.get().send(sender)
                } else {
                    Message.CHEST_ALREADY_DISABLED.get().send(sender)
                }
                return true
            }

            "help" -> Help.sendHelp(sender)
            else -> {
                Message.MESSAGE_UNKNOWN.get().send(sender)
                return true
            }
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        var arguments = emptyList<String>()
        val completions = ArrayList<String>()

        if (args.size == 1) {
            arguments =
                listOf("setlocation", "help", "remove", "enable", "disable", "opengui", "chest", "lootpool")
            StringUtil.copyPartialMatches(args[0], arguments, completions)
        }
        if (args.size == 2) {
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
            }
            StringUtil.copyPartialMatches(args[1], arguments, completions)
        }
        if (args.size == 3) {
            if ((args[0].lowercase() == "lootpool" || args[0].lowercase() == "rewards") && args[1].lowercase() == "add") {
                arguments = Material.values().map {
                    it.toString()
                }
                StringUtil.copyPartialMatches(args[2], arguments, completions)
            }
        }
        completions.sort()
        return completions
    }
}