package de.skycave.shoprotation.utils

import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.enums.Message
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object Help {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun sendHelp(player: Player) {
        Message.CHEST_SET_LOCATION.get().send(player)
        Message.CHEST_OPEN_GUI.get().send(player)
        Message.CHEST_DELETE_ITEMS.get().send(player)
        Message.CHEST_SHOW_ITEMS.get().send(player)
        Message.CHEST_SHOW_CURRENT_ITEM.get().send(player)
        Message.CHEST_ENABLE.get().send(player)
        Message.CHEST_DISABLE.get().send(player)
        Message.CHEST_HELP.get().send(player)
    }
}