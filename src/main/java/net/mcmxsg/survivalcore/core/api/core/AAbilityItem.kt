package net.mcmxsg.survivalcore.core.api.core

import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.listener.PListener
import net.bxx2004.pandalib.bukkit.util.PPlugin
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.data.ItemList
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.getAbility
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.hasAbility
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.writeAbility
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.MainHand

abstract class AAbilityItem : BlockItem{
    abstract fun temple() : PItemStack
    open fun interact(e:PlayerInteractEvent){}
    open fun f(e: PlayerUseFEvent){}
    open fun held(e:PlayerItemHeldEvent){}
    open fun consume(e:PlayerItemConsumeEvent){}
    open fun attack(hand: MainHand, e:EntityDamageByEntityEvent){}
    abstract override fun name() : String
    override fun asItem() : PItemStack{
        return PItemStack(temple().writeAbility(name()))
    }
}