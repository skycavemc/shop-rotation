package de.skycave.shoprotation.utils

import org.bukkit.Location
import java.text.NumberFormat

object Formatting {

    fun formatLocation(location: Location): String {
        return "x: ${location.x.toInt()}, y: ${location.y.toInt()}, z: ${location.z.toInt()}, world: ${location.world.name}"
    }
}