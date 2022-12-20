package de.skycave.shoprotation.utils

import org.bukkit.Bukkit
import org.bukkit.Location

object Formatting {

    fun formatLocation(location: Location): String {
        return "x: ${location.x.toInt()}, y: ${location.y.toInt()}, z: ${location.z.toInt()}, world: ${location.world.name}"
    }

    fun reverseFormatLocation(formattedLocation: String): Location {
        val delimx = "x: "
        val delimy = ", y: "
        val delimz = ", z: "
        val delimwold = ", wold: "

        val parts = formattedLocation.split(delimx, delimy, delimz, delimwold)

        return Location(Bukkit.getWorld(parts[3]), parts[0].toDouble(), parts[1].toDouble(), parts[2].toDouble())
    }
}