package net.mcmxsg.survivalcore.core.interact

import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.bxx2004.pandalib.bukkit.listener.BukkitListener
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import org.bukkit.Material
import org.bukkit.block.data.type.Door
import org.bukkit.block.data.type.TrapDoor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

@BukkitListener
class IDoor : Listener{
    @EventHandler
    fun onInteract(e: PlayerInteractEvent){
        if (e.action == Action.RIGHT_CLICK_BLOCK){
            if (e.clickedBlock != null && e.clickedBlock!!.type != Material.AIR){
                if (e.clickedBlock!!.type.toString().endsWith("DOOR")){
                    e.isCancelled = true
                    PTitle.To(e.player,"&c门已经无法打开,请按F撬锁")
                }
            }
        }
    }
    @EventHandler
    fun onSwap(e: PlayerUseFEvent) {
        val player = e.player
        if (player.getTargetBlockExact(3) != null && player.getTargetBlockExact(3)!!.type != Material.AIR) {
            if (player.getTargetBlockExact(3)!!.type.toString().endsWith("DOOR")) {
                e.isCancelled = true
                try {
                    val door = player.getTargetBlockExact(3)!!.blockData as Door
                    door.isOpen = !door.isOpen
                    player.getTargetBlockExact(3)!!.blockData = door
                }catch (e:Exception){
                    val door = player.getTargetBlockExact(3)!!.blockData as TrapDoor
                    door.isOpen = !door.isOpen
                    player.getTargetBlockExact(3)!!.blockData = door
                }
            }
        }
    }
}