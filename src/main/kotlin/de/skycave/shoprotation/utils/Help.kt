package de.skycave.shoprotation.utils

import de.skycave.shoprotation.ShopRotation
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object Help {

    private val main = JavaPlugin.getPlugin(ShopRotation::class.java)

    fun sendHelp(player: Player) {
        main.messages.get("chest-set-location").send(player)
        main.messages.get("chest-open-gui").send(player)
        main.messages.get("chest-delete-items").send(player)
        main.messages.get("chest-show-items").send(player)
        main.messages.get("chest-show-current-item").send(player)
        main.messages.get("chest-enable").send(player)
        main.messages.get("chest-disable").send(player)
        main.messages.get("chest-help").send(player)
    }
}