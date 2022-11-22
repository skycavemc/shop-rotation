package de.skycave.shoprotation.model.display

import org.bukkit.ChatColor

enum class GUIView(private val title: String) {

    MAIN("&7» &1Hauptmenü"),

    //REWARDS
    REWARDS("&7» &dRewards"),
    //REWARDS_ADD("&7» &dRewards &8 - &7Add Rewards"),
    REWARDS_REMOVE("&7» &dRewards &8 - &7Remove Rewards"),

    //LOOTPOOL
    LOOTPOOL("&7» &6Items"),
    //LOOTPOOL_ADD("&7» &6Items &8 - &7Add Items"),
    LOOTPOOL_REMOVE("&7» &6Items &8 - &7Remove Items")

    ;

    fun getTitle(): String {
        return ChatColor.translateAlternateColorCodes('&', title)
    }
}