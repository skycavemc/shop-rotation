package de.skycave.shoprotation.model

import de.skycave.shoprotation.ShopRotation
import de.skycave.shoprotation.exceptions.HologramAlreadyDestroyedException
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask

class Hologram(
    var location: Location,
    var title: String,
    var item: ItemStack,
    main: ShopRotation
) {
    private lateinit var armorStand: ArmorStand
    private lateinit var displayItem: Item

    private val checkLivingTask: BukkitTask
    private var isShown = false
    private var isDestroyed = false

    init {
        checkLivingTask = main.server.scheduler.runTaskTimer(
            main,
            Runnable {
                if(!isShown) return@Runnable
                if(armorStand.isDead || displayItem.isDead) {
                    kill()
                    create()
                }
            }, 0, 200
        )
    }

    fun create() {
        if (isDestroyed) throw HologramAlreadyDestroyedException()

        val holoLoc = location.clone().add(0.5, -0.5, 0.5)
        armorStand = holoLoc.world.spawnEntity(holoLoc, EntityType.ARMOR_STAND) as ArmorStand
        armorStand.isVisible = false
        armorStand.isCustomNameVisible = true
        armorStand.setGravity(false)
        armorStand.customName(Component.text(title))
        armorStand.isInvulnerable = true
        armorStand.canPickupItems = false

        val itemLoc = holoLoc.clone().add(0.0, -0.2, 0.0)
        displayItem = itemLoc.world.spawnEntity(itemLoc, EntityType.DROPPED_ITEM) as Item
        displayItem.itemStack = item
        displayItem.setGravity(false)
        displayItem.pickupDelay = Int.MAX_VALUE
        displayItem.ticksLived = Int.MAX_VALUE
        armorStand.addPassenger(displayItem)

        isShown = true
    }

    fun kill() {
        armorStand.isInvulnerable = false
        armorStand.removePassenger(displayItem)
        displayItem.isInvulnerable = false
        displayItem.remove()
        armorStand.remove()

        isShown = false
    }

    fun destroy() {
        kill()
        checkLivingTask.cancel()
        isDestroyed = true
    }


}