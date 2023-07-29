package net.mcmxsg.survivalcore.core.api.core

import de.tr7zw.changeme.nbtapi.NBTBlock
import de.tr7zw.changeme.nbtapi.NBTCompound
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.listener.PListener
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.core.api.event.ItemAccessEvent
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.clearMachine
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.getMachine
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.hasMachine
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.writeMachine
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.getMachine
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.hasMachine
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.writeMachine
import net.mcmxsg.survivalcore.util.intofunction.PlayerFunction.hasInteractPermission
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

abstract class ABlockMachine : BlockItem{
    abstract override fun name() : String
    abstract fun temple() : PItemStack
    override fun asItem() : PItemStack{
        return temple().writeMachine(name())
    }
    open fun blockPlace(e:BlockPlaceEvent) : BlockPlaceEvent{return e}
    open fun blockBreak(e:BlockBreakEvent) : BlockBreakEvent{return e}
    open fun interact(e:PlayerInteractEvent){}
    open fun f(e: PlayerUseFEvent){}
    open fun onBlock(block: Block,player:Player){}
    open fun accessItem(e: ItemAccessEvent){}
    fun data(block: Block) : NBTCompound{
        val nbt : NBTBlock = NBTBlock(block)
        if (!nbt.data.getCompound("survivalcore").hasTag("machinedata")){
            nbt.data.getCompound("survivalcore").addCompound("machinedata")
        }
        return nbt.data.getCompound("survivalcore").getCompound("machinedata")
    }
}