package net.mcmxsg.survivalcore.core.interact

import net.bxx2004.pandalib.bukkit.language.abandon.PActionBar
import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.bxx2004.pandalib.bukkit.listener.BukkitListener
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAttemptPickupItemEvent

@BukkitListener
class IPick : Listener{
    @EventHandler
    fun onPick(e:PlayerAttemptPickupItemEvent){
        e.isCancelled = true
        PActionBar.To(e.player,"&7无法捡起物品,请按F键捡起物品")
    }
    @EventHandler
    fun onInteract(e: PlayerUseFEvent){
        val player = e.player
        if (player.getTargetBlockExact(3) == null){
            return
        }
        val location = player.getTargetBlockExact(3)!!.location
        var item: Item? = null
        location.getNearbyEntitiesByType(Item::class.java, 1.1).forEach(){
            item = it
        }
        if (item == null){
            return
        }
        if (player.inventory.firstEmpty() == -1){
            PTitle.To(player,"&7背包已经满了,无法捡起物品")
            return
        }
        e.isCancelled = true
        item!!.remove()
        player.inventory.addItem(item!!.itemStack)
    }
}