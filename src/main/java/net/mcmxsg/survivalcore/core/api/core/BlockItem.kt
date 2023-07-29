package net.mcmxsg.survivalcore.core.api.core

import net.bxx2004.pandalib.bukkit.item.PItemStack

interface BlockItem {
    fun asItem(): PItemStack
    fun name() : String
}