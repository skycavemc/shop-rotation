package de.skycave.shoprotation

import de.skycave.shoprotation.command.ShopRotationCommand
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class ShopRotation : JavaPlugin(){

    override fun onEnable() {

        registerCommand("shoprotation", ShopRotationCommand(this))
        registerEvents()

    }

    private fun registerCommand(cmd: String, executor: CommandExecutor) {
        val command = getCommand(cmd)
        if (command == null) {
            logger.severe("No entry for command $cmd found in the plugin.yml.")
            return
        }
        command.setExecutor(executor)
    }

    private fun registerEvents(vararg events: Listener) {
        for (event in events) {
            server.pluginManager.registerEvents(event, this)
        }
    }

    override fun onDisable() {

    }

}