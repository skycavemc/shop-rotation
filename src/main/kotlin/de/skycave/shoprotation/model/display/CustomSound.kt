package de.skycave.shoprotation.model.display

import de.skycave.shoprotation.ShopRotation
import org.bukkit.Sound
import org.bukkit.entity.Player

enum class CustomSound(private val sound: Sound, private val pitch: Float) {

    ERROR(Sound.ENTITY_ITEM_BREAK, 0.7f),
    SUCCESS(Sound.ENTITY_PLAYER_LEVELUP, 1.2f),
    CLICK(Sound.UI_BUTTON_CLICK, 1.0f),
    CHEST_OPEN(Sound.BLOCK_CHEST_OPEN,1.0f);

    fun playTo(player: Player) {
        player.playSound(player.location, sound, ShopRotation.MASTER_VOLUME, pitch)
    }

}