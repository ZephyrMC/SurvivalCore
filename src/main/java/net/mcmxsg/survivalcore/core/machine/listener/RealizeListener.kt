package net.mcmxsg.survivalcore.core.machine.listener

import net.bxx2004.pandalib.bukkit.listener.BukkitListener
import net.mcmxsg.survivalcore.core.api.core.StorageBlock
import net.mcmxsg.survivalcore.core.api.core.Upgradable
import net.mcmxsg.survivalcore.core.api.event.ItemAccessEvent
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.data.MachineList
import net.mcmxsg.survivalcore.data.save.BlockYml
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.clearMachine
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.getMachine
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.hasMachine
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.writeMachine
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.getMachine
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.hasMachine
import net.mcmxsg.survivalcore.util.intofunction.PlayerFunction.hasInteractPermission
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

@BukkitListener
class RealizeListener : Listener{
    @EventHandler
    fun onInteract(e: PlayerInteractEvent){
        if (e.player.hasInteractPermission()){
            if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.LEFT_CLICK_BLOCK){
                var block = e.clickedBlock
                if (block!!.hasMachine()){
                    if (MachineList.searchMachine(block.getMachine()!!) != null){
                        MachineList.searchMachine(block.getMachine()!!)!!.interact(e)
                    }
                }
            }
        }
    }
    @EventHandler
    fun onAccessItem(e: ItemAccessEvent){
        if (e.source is Player){
            val player = e.source as Player
            if (player.hasInteractPermission()){
                if (MachineList.searchMachine(e.machine.name()) != null){
                    MachineList.searchMachine(e.machine.name())!!.accessItem(e)
                }
            }
        }else{
            if (MachineList.searchMachine(e.machine.name()) != null){
                MachineList.searchMachine(e.machine.name())!!.accessItem(e)
            }
        }
    }
    @EventHandler
    fun placeEvent(e: BlockPlaceEvent){
        val block = e.block
        if (e.itemInHand.hasMachine()){
            block.writeMachine(e.itemInHand.getMachine()!!)
            if (e.player.hasInteractPermission()){
                if (MachineList.searchMachine(e.itemInHand.getMachine()!!) != null){
                    var va = MachineList.searchMachine(e.itemInHand.getMachine()!!)!!.blockPlace(e)
                    if (va != null && va!!.isCancelled){
                        block.clearMachine()
                    }
                    if (MachineList.searchMachine(e.itemInHand.getMachine()!!) is StorageBlock){
                        var sb = MachineList.searchMachine(e.itemInHand.getMachine()!!) as StorageBlock
                        sb.onPlace(BlockYml(block))
                    }
                }
            }else{
                e.isCancelled = true
            }
        }
    }
    @EventHandler
    fun onBreak(e: BlockBreakEvent){
        if (e.block.hasMachine()){
            if (e.player.hasInteractPermission()){
                if (MachineList.searchMachine(e.block.getMachine()!!) != null){
                    e.isDropItems = false
                    val ve = MachineList.searchMachine(e.block.getMachine()!!)!!.blockBreak(e)
                    if ((ve != null) && !ve.isCancelled){
                        e.block.world.dropItem(e.block.location,
                            Upgradable.searchMachine(e.block.getMachine()!!)!!.asItem())
                        if (MachineList.searchMachine(e.block.getMachine()!!) is StorageBlock){
                            var sb = MachineList.searchMachine(e.block.getMachine()!!) as StorageBlock
                            sb.onRemove(BlockYml(e.block))
                        }
                        e.block.clearMachine()
                    }
                }
            }else{
                e.isCancelled = true
            }
        }
    }
    @EventHandler
    fun onMove(e: PlayerMoveEvent){
        if (!e.hasChangedBlock()){
            return
        }
        if (e.to.clone().add(0.00,-1.00,0.00).block.hasMachine()){
            if (MachineList.searchMachine(e.to.clone().add(0.00,-1.00,0.00).block.getMachine()!!) != null){
                MachineList.searchMachine(e.to.clone().add(0.00,-1.00,0.00).block.getMachine()!!)!!.onBlock(e.to.clone().add(0.00,-1.00,0.00).block,e.player)
            }
        }
    }
    @EventHandler
    fun onF(e: PlayerUseFEvent){
        val player = e.player
        if (player.getTargetBlockExact(3) != null && player.getTargetBlockExact(3)!!.type != Material.AIR) {
            if (player.getTargetBlockExact(3)!!.hasMachine()) {
                if (MachineList.searchMachine(player.getTargetBlockExact(3)!!.getMachine()!!) != null){
                    MachineList.searchMachine(player.getTargetBlockExact(3)!!.getMachine()!!)!!.f(e)
                }
            }
        }
    }
}