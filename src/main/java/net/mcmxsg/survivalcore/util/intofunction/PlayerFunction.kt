package net.mcmxsg.survivalcore.util.intofunction

import org.bukkit.block.Block
import org.bukkit.entity.Player

object PlayerFunction {
    fun Player.hasInteractPermission() : Boolean{
        return true
    }
    fun Player.targetBlock() : Block{
        return this.getTargetBlockExact(3)!!
    }
    fun Player.targetMachine() : Block{
        return this.getTargetBlockExact(3)!!
    }
}