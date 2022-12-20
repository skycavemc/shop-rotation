package de.skycave.shoprotation.listener

import com.mongodb.client.model.Filters
import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.utils.Utils
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class PlayerInteractListener(private val main: ShopRotation) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.hand == EquipmentSlot.HAND) {
            val clickedBlock = event.clickedBlock ?: return
            if (clickedBlock.type != Material.CHEST) {
                return
            }
            val chest =
                main.chests.find(Filters.eq("location", clickedBlock.location.toBlockLocation())).first() ?: return
            event.isCancelled = true
            Utils.openGUIMain(player, chest.name)
        }
    }
}