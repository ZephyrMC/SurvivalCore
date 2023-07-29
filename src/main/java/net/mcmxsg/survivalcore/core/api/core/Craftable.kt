package net.mcmxsg.survivalcore.core.api.core

import net.bxx2004.pandalib.bukkit.item.PItemStack
import org.bukkit.entity.Player

interface Craftable<T : BlockItem> {
    fun material() : ArrayList<PItemStack>
    fun time() : Int
    fun chance(player: Player,item: T) : Boolean
    fun require(player: Player) : Boolean
}