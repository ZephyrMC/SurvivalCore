package net.mcmxsg.survivalcore.core.api.core

import net.mcmxsg.survivalcore.core.alilityitem.abilityitems.Drawing
import org.bukkit.entity.Player

interface Forgeable {
    fun drawing() : Drawing
    fun forgeNumber() : Int
    fun chance(player: Player) : Boolean
    fun require(player: Player) : Boolean
}