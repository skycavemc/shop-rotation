package de.skycave.shoprotation.Enums

import org.bukkit.ChatColor

enum class Message(private val message: String) {

    PREFIX("&d&l| &6ShopRotation &8»"),
    NO_PERMS("&cDu hast keine Rechte für diesen Befehl."),
    INVALID_NUMBER("&c%number ist keine gültige Zahl."),
    INVALID_MATERIAL("&cBitte gib ein gültiges Material an."),
    NO_PLAYER("&cDieser Befehl ist nur für Spieler."),

    ;

    fun getFormatted(): String {
        return ChatColor.translateAlternateColorCodes('&', message)
    }

    fun getMessage(): String {
        return ChatColor.translateAlternateColorCodes('&', PREFIX.message + message)
    }
}