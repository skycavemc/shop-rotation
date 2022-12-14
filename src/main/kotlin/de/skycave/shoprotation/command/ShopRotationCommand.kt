package de.skycave.shoprotation.command

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import de.skycave.shoprotation.ShopRotation
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
            main.messages.get("no-player").send(sender)
            return true
        }
        if (!sender.hasPermission("skybee.shoprotation.admin")) {
            main.messages.get("no-perms").send(sender)
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
                    main.messages.get("set-location-syntax").send(sender)
                    return true
                }
                val name = args[1].lowercase()
                val filter = Filters.eq("name", name)
                var chest = main.chests.find(filter).first()

                if (chest == null) {
                    chest = Chest()
                    chest.name = name
                    chest.location = sender.location

                    main.registerChests()

                    main.chests.insertOne(chest)
                    main.registerChests()
                    main.messages.get("chest-created-success")
                        .replace("%name", name)
                        .replace("%location", Formatting.formatLocation(sender.location))
                        .send(sender)
                    return true
                }
                main.chests.updateOne(Filters.eq("name", name), Updates.set("location", sender.location))
                main.messages.get("set-location-success")
                    .replace("%name", name)
                    .replace("%location", Formatting.formatLocation(sender.location))
                    .send(sender)
                return true
            }

            "opengui" -> {
                //shoprotation opengui <name>
                if (args.size < 2) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                val name = args[1]
                Utils.openGUIMain(sender, name)
            }

            "remove" -> {
                //shoprotation remove <name>
                if (args.size < 2) {
                    main.messages.get("not-enough-arguments").send(sender)
                }
                val name = args[1]
                val filter = Filters.eq("name", name)
                main.chests.deleteOne(filter)

                //TODO: Abfrage ob die Chest existiert
                main.registerChests()
                //main.messages.get("chest-remove-success").send(sender)

            }

            "lootpool" -> {
                //SubCommand
                if (args.size < 2) {
                    Help.sendHelp(sender)
                    return true
                }
                if (!sender.hasPermission("skybee.shoprotation.admin")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                return ShopRotationLootpoolCommand().apply(sender, args)
            }

            "rewards" -> {
                //SubCommand
                if (args.size < 2) {
                    Help.sendHelp(sender)
                    return true
                }
                if (!sender.hasPermission("skybee.shoprotation.admin")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                return ShopRotationRewardsCommand().apply(sender, args)
            }

            "enable" -> {
                //shoprotation enable <name>
                if (args.size < 2) {
                    main.messages.get("set-enabled-syntax").send(sender)
                    return true
                }
                val name = args[1].lowercase()
                if (name == "all") {
                    main.chests.updateMany(Filters.exists("name"), Updates.set("enabled", true))
                    main.messages.get("enabled-all").send(sender)
                    return true
                }

                val filter = Filters.eq("name", name)
                val chest = main.chests.find(filter).first()

                if (chest == null) {
                    main.messages.get("chest-unknown").replace("%name", name).send(sender)
                    return true
                }
                if (!chest.enabled) {
                    main.chests.updateOne(Filters.eq("name", name), Updates.set("enabled", true))
                    main.messages.get("set-enabled-success").send(sender)
                } else {
                    main.messages.get("already-enabled").send(sender)
                }
                return true
            }

            "disable" -> {
                //shoprotation disable <name>
                if (args.size < 2) {
                    main.messages.get("set-disabled-syntax").send(sender)
                    return true
                }
                val name = args[1].lowercase()
                if (name == "all") {
                    main.chests.updateMany(Filters.exists("name"), Updates.set("enabled", false))
                    main.messages.get("disabled-all").send(sender)
                    return true
                }

                val filter = Filters.eq("name", name)
                val chest = main.chests.find(filter).first()

                if (chest == null) {
                    main.messages.get("chest-unknown").replace("%name", name).send(sender)
                    return true
                }
                if (chest.enabled) {
                    main.chests.updateOne(Filters.eq("name", name), Updates.set("enabled", false))
                    main.messages.get("set-disabled-success").send(sender)
                } else {
                    main.messages.get("already-disabled").send(sender)
                }
                return true
            }

            "help" -> Help.sendHelp(sender)
            else -> {
                main.messages.get("message-unknown").send(sender)
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
                listOf("setlocation", "help", "remove", "enable", "disable", "opengui", "chest", "lootpool", "rewards")
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

                "rewards" -> {
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