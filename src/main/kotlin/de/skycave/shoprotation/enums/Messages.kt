package de.skycave.shoprotation.enums;

import de.skycave.shoprotation.ShopRotation
import de.skycave.skycavelib.models.ChatMessage
import org.bukkit.plugin.java.JavaPlugin

enum class Message(private val string: String) {

    NO_PERMS("&cDu hast keine Rechte für diesen Befehl."),
    INVALID_NUMBER("&c%number ist keine gültige Zahl."),
    INVALID_MATERIAL("&cBitte gib ein gültiges Material an."),
    NO_PLAYER("&cDieser Befehl ist nur für Spieler."),
    MESSAGE_UNKNOWN("&cUnbekannter Befehl. Siehe /shoprotation help"),
    HELP_HELP("&6/advent help&8: &7Zeigt Hilfe an."),

    UNKNOWN_INVENTORY("&cDas angefragte Inventar existiert nicht."),
    NOT_ENOUGH_ARGS("&cZu wenige Argumente! Siehe /shoprotation help"),
    MATERIAL_AIR_NOT_ALLOWED("&cDas Material \"AIR\" ist nicht erlaubt."),

    CHEST_UNKNOWN("&cDie Chest %name wurde nicht gefunden."),
    CHEST_REMOVE_SUCESS("&aDie Chest %name wurde erfolgreich entfernt."),
    CHEST_IS_EMPTY("&cEs wurden keine Items in \"%name\" festgelegt."),

    CHEST_SET_LOCATION("&e/shoprotation setlocation <name> &8» &8Setzt den Ort für die Chest"),
    CHEST_OPEN_GUI("&e/shoprotation open &8» &8Öffnet das Inventar der Chest"),
    CHEST_DELETE_ITEMS("&e/shoprotation delete &8» &8Löscht alle Items der Chest"),
    CHEST_SHOW_ITEMS("&e/shoprotation items &8» &8Zeigt alle Items an"),
    CHEST_SHOW_CURRENT_ITEM("&e/shoprotation current &8» &8Zeigt das aktuelle Item an"),
    CHEST_ENABLE("&e/shoprotation enable &8» &8Aktiviert die Chest"),
    CHEST_DISABLE("&e/shoprotation disable &8» &8Deaktiviert die Chest"),
    CHEST_HELP("&e/shoprotation help &8» &8Zeigt diesen Text an"),
    COULD_NOT_FIND_CHEST("&eKonnte die angegebene Kiste nicht finden"),

    LOCATION_SET_SUCESS("&aOrt von \"%name\" wurde erfolgreich gesetzt. &7(%location)"),
    CHEST_CREATED_SUCESSUL("&aChest \"%name\" wurde erstellt. &7(%location)"),
    LOCATION_SYNTAX("&e/shoprotation setlocation <name>"),
    LOOK_AT_CHEST("&eBitte schaue auf eine Chest"),


    CHEST_ENABLED_SUCESS("&7Die Chest wurde erfolgreich &aAktiviert&7."),
    CHEST_ENABLED_ALL("&7Alle Chests wurden &aaktiviert&7."),
    CHEST_DISABLED_SUCESS("&7Die Chest wurde erfolgreich &cDeaktiviert&7."),
    CHEST_DISABLED_ALL("&7Alle Chests wurden &cdeaktiviert&7."),
    CHEST_ALREADY_ENABLED("&cDie Chest ist schon &aActiviert&c."),
    CHEST_ALREADY_DISABLED("&cDie Chest ist schon &4Deaktiviert&c."),
    CHEST_SET_ENABLED("&e/shoprotation enable <all/name>"),
    CHEST_SET_DISABLED("&e/shoprotation disable <all/name>"),

    CHEST_CURRENT_ITEM("&eCurrent item: &f %currentitem &7(&f%amount&7/&f%requiredamount&7)"),

    LOAD_LOOTPOOL_ERROR("&cUnable to load LOOTPOOL (ChestItems) - DB."),
    ADD_ITEM_TO_LOOTPOOL("&eDas Item (&f&o%material&e, &f&o%amount&e) wurde erfolgreich bei &fZiele &ehinzugefügt.");

    fun get(): ChatMessage {
        return ChatMessage(JavaPlugin.getPlugin(ShopRotation::class.java).prefix, string)
    }

}
