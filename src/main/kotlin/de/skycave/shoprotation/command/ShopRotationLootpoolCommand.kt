package de.skycave.shoprotation.command

import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.model.display.GUIView
import de.skycave.shoprotation.utils.Utils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ShopRotationLootpoolCommand: java.util.function.BiFunction<CommandSender, Array<out String>, Boolean> {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    override fun apply(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            main.messages.get("no-player").send(sender)
            return true
        }
        when (args[1].lowercase()) {
            "addhanditem" -> {

            }
            "add" -> {

            }
            "remove" -> {
                if(!sender.hasPermission("skybee.shoprotation.lootpool.remove")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                if(args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                Utils.openGUILootpool(sender, GUIView.LOOTPOOL_REMOVE, args)
                return true
            }
            "show" -> {
                if(!sender.hasPermission("skybee.shoprotation.lootpool.show")) {
                    main.messages.get("no-perms").send(sender)
                    return true
                }
                if(args.size < 3) {
                    main.messages.get("not-enough-arguments").send(sender)
                    return true
                }
                Utils.openGUILootpool(sender, GUIView.LOOTPOOL, args)
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