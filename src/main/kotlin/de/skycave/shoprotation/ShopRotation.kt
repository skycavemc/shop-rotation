package de.skycave.shoprotation

import de.skycave.shoprotation.command.ShopRotationCommand
import de.skycave.skycavelib.annotations.Prefix
import de.skycave.skycavelib.data.MessageRegistry
import de.skycave.skycavelib.models.SkyCavePlugin

@Prefix("&fSky&3Cave &8» ")
class ShopRotation : SkyCavePlugin() {

    val messages = MessageRegistry(this)

    override fun onEnable() {

        registerCommand("shoprotation", ShopRotationCommand(this))
        registerEvents()
    }

    override fun onDisable() {

    }

    private fun registerMessages() {
        val messages = mapOf(
            //global messages
            "no-perms" to "&cDu hast keine Rechte für diesen Befehl.",
            "invalid-number" to "&c%number ist keine gültige Zahl.",
            "invalid-material" to "&cBitte gib ein gültiges Material an.",
            "no-player" to "&cDieser Befehl ist nur für Spieler.",
            "message-unknown" to "&cUnbekannter Befehl. Siehe /shoprotation help",

            //info messages

            //location messages
            "set-location-success" to "&aOrt wurde erfolgreich gesetzt. &7(%x, %y, %z, %direction)",

            )
    }

}