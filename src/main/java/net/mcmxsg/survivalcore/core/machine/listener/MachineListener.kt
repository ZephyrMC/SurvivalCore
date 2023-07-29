package net.mcmxsg.survivalcore.core.machine.listener

import net.bxx2004.pandalib.bukkit.listener.BukkitListener
import net.bxx2004.pandalib.bukkit.listener.event.PandaLibExtendEvent
import net.mcmxsg.survivalcore.core.api.event.ItemAccessEvent
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import net.mcmxsg.survivalcore.data.Config
import net.mcmxsg.survivalcore.data.MachineList
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.getMachine
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.hasMachine
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.player.PlayerDropItemEvent

@BukkitListener
class MachineListener : Listener {
    @EventHandler
    fun breakblock(e:BlockBreakEvent){
        if (e.block.world.name != "spawn") return
        if (!e.player.isOp){
            e.isCancelled = true
        }
    }
    @EventHandler
    fun place(e:BlockPlaceEvent){
        if (e.block.world.name != "spawn") return
        if (!e.player.isOp){
            e.isCancelled = true
        }
    }
    private fun getMinMachine(block: Block, radius:Int) : Block {
        for (x in -radius..radius) {
            for (y in -radius..radius){
                for (z in -radius..radius) {
                    val blockAt = block.world.getBlockAt(
                        block.location.clone().add(
                            x.toDouble(), y.toDouble(), z.toDouble()
                        )
                    )
                    if (blockAt.hasMachine()){
                        return blockAt
                    }
                }
            }
        }
        return block.location.clone().add(0.00,-2.00,0.00).block
    }
    @EventHandler
    fun onDrop(e: EntityDropItemEvent){
        var block:Block = getMinMachine(e.itemDrop.location.block,Config.accessItemDistance)
        if (block.hasMachine()){
            PandaLibExtendEvent.callPandaLibEvent(ItemAccessEvent(MachineList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map {it.isAccessible = true; it.get(null) as ABlockMachine }.filter { it.name().equals(
                block.getMachine()
            ) }[0], e.itemDrop,e.entity,block))
        }
    }
    @EventHandler
    fun onDrop(e: PlayerDropItemEvent){
        var block:Block = getMinMachine(e.itemDrop.location.block,Config.accessItemDistance)
        if (block.hasMachine()){
            PandaLibExtendEvent.callPandaLibEvent(ItemAccessEvent(MachineList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map {it.isAccessible = true; it.get(null) as ABlockMachine }.filter { it.name().equals(
                block.getMachine()
            ) }[0],e.itemDrop,e.player,block))
        }
    }
}