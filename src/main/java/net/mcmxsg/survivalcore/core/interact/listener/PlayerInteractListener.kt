package net.mcmxsg.survivalcore.core.interact.listener

import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.bxx2004.pandalib.bukkit.listener.BukkitListener
import net.bxx2004.pandalib.bukkit.listener.event.PandaLibExtendEvent
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.util.intofunction.PlayerFunction.hasInteractPermission
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent

@BukkitListener
class PlayerInteractListener : Listener {
    @EventHandler
    fun void(e:PlayerSwapHandItemsEvent){
        if (e.player.hasInteractPermission()){
            e.isCancelled = true
            PandaLibExtendEvent.callPandaLibEvent(PlayerUseFEvent(e.player))
        }else{
            PTitle.To(e.player,"&c你没有交互权限")
        }
    }
}