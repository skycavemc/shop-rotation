package de.skycave.shoprotation.listener

import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.utils.Utils
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener(private val main: ShopRotation) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val clickedBlock = event.clickedBlock ?: return

            if (main.saveChestsLocation.contains(clickedBlock.location)) {
                val clickedChest = main.saveChestsLocation[clickedBlock.location]
                if (clickedChest != null) {
                    Utils.openGUIMain(player, clickedChest)
                }
            }
        }
    }
}