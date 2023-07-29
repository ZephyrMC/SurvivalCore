package net.mcmxsg.survivalcore.core.api.event

import net.bxx2004.pandalib.bukkit.listener.event.PandaLibExtendEvent
import org.bukkit.entity.Player

class PlayerUseFEvent(player: Player) : PandaLibExtendEvent(){
    var player : Player
    init {
        this.player = player
    }
}