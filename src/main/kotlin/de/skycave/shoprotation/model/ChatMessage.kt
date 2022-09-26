package de.skycave.shoprotation.model

import de.skycave.shoprotation.ShopRotation
import de.skycave.skycavelib.models.SkyCavePlugin
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/**
 * Class that stores a raw string and returns the string with translated color codes and prefix.
 */
class ChatMessage
/**
 * Creates a new colored string instance from a PrefixHolder instance and a base string.
 * @param prefixHolder A class that holds a prefix
 * @param base The raw string to save
 */(private val prefixHolder: SkyCavePlugin, private var base: String) {
    /**
     * Replaces the first occurrence of the given string with the second given string.
     * @param from String to replace
     * @param to New string
     * @return The ColoredString instance
     */
    fun replace(from: String, to: String): ChatMessage {
        base = base.replace(from, to)
        return this
    }

    /**
     * Replaces all occurrences of the given string with the second given string.
     * @param from String to replace
     * @param to New string
     * @return The ColoredString instance
     */
    fun replaceAll(from: String, to: String): ChatMessage {
        while (base.contains(from)) {
            base = base.replace(from, to)
        }
        return this
    }

    /**
     * Gets the string with prefix and translated Minecraft color codes.
     * @return The string output
     */
    fun get(): String {
        base = ChatColor.translateAlternateColorCodes('&', prefixHolder.prefix + base)
        return base
    }

    /**
     * Gets the string with translated Minecraft color codes and optionally with prefix.
     * @param prefix Whether to start with the prefix of the prefix holder
     * @return The string output
     */
    operator fun get(prefix: Boolean): String {
        if (prefix) {
            base = prefixHolder.prefix + base
        }
        base = ChatColor.translateAlternateColorCodes('&', base)
        return base
    }

    /**
     * Gets the string, optionally with prefix and optionally with translated Minecraft color codes.
     * @param prefix Whether to start with the prefix of the prefix holder
     * @param formatted Whether to translate Minecraft color codes
     * @return The string output
     */
    operator fun get(prefix: Boolean, formatted: Boolean): String {
        if (prefix) {
            base = prefixHolder.prefix + base
        }
        if (formatted) {
            base = ChatColor.translateAlternateColorCodes('&', base)
        }
        return base
    }

    /**
     * Directly sends the message result to the specified CommandSender.
     * @param sender The receiver
     */
    fun send(sender: CommandSender) {
        sender.sendMessage(get())
    }

    /**
     * Directly sends the message result to the specified CommandSender, optionally with prefix.
     * @param sender The receiver
     * @param prefix Whether to start with the prefix of the prefix holder
     */
    fun send(sender: CommandSender, prefix: Boolean) {
        sender.sendMessage(get(prefix))
    }

    /**
     * Directly sends the message result to the specified CommandSender, optionally with prefix
     * and optionally with translated Minecraft color codes.
     * @param sender The receiver
     * @param prefix Whether to start with the prefix of the prefix holder
     * @param formatted Whether to translate Minecraft color codes
     */
    fun send(sender: CommandSender, prefix: Boolean, formatted: Boolean) {
        sender.sendMessage(get(prefix, formatted))
    }
}