package net.mcmxsg.survivalcore.core.alilityitem.listener

import net.bxx2004.pandalib.bukkit.listener.BukkitListener
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.data.ItemList
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.getAbility
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.hasAbility
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.MainHand

@BukkitListener
class RealizeListener : Listener{
    @EventHandler
    fun onDamage(e: EntityDamageByEntityEvent){
        if (e.damager is Player){
            var p = e.damager as Player
            if (p.inventory.itemInMainHand == null || p.inventory.itemInMainHand.type == Material.AIR){
                return
            }
            if (p.inventory.itemInMainHand.hasAbility() && ItemList.searchItem(p.inventory.itemInMainHand.getAbility()!!) != null){
                ItemList.searchItem(p.inventory.itemInMainHand.getAbility()!!)!!.attack(MainHand.RIGHT,e)
            }
        }else{
            return
        }
    }
    @EventHandler
    fun onDamageA(e: EntityDamageByEntityEvent){
        if (e.damager is Player){
            var p = e.damager as Player
            if (p.inventory.itemInOffHand == null || p.inventory.itemInOffHand.type == Material.AIR){
                return
            }
            if (p.inventory.itemInOffHand.hasAbility() && ItemList.searchItem(p.inventory.itemInOffHand.getAbility()!!) != null){
                ItemList.searchItem(p.inventory.itemInOffHand.getAbility()!!)!!.attack(MainHand.RIGHT,e)
            }
        }else{
            return
        }
    }
    @EventHandler
    fun onInteract(e: PlayerInteractEvent){
        if (e.item == null){
            return
        }
        if (e.item!!.hasAbility() && ItemList.searchItem(e.item!!.getAbility()!!) != null){
            ItemList.searchItem(e.item!!.getAbility()!!)!!.interact(e)
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    fun onf(e: PlayerUseFEvent){
        if (e.player.inventory.itemInMainHand != null){
            if (e.player.inventory.itemInMainHand.type != Material.AIR){
                if (e.player.inventory.itemInMainHand.hasAbility() && ItemList.searchItem(e.player.inventory.itemInMainHand!!.getAbility()!!) != null){
                    ItemList.searchItem(e.player.inventory.itemInMainHand!!.getAbility()!!)!!.f(e)
                }
            }
        }
    }
    @EventHandler
    fun c(e: PlayerItemConsumeEvent){
        if (e.item == null){
            return
        }
        if (e.item!!.hasAbility() && ItemList.searchItem(e.item!!.getAbility()!!) != null){
            ItemList.searchItem(e.item!!.getAbility()!!)!!.consume(e)
        }
    }
    @EventHandler
    fun onheld(e: PlayerItemHeldEvent){
        val item = e.player.inventory.getItem(e.newSlot)
        if (item != null){
            if (item.hasAbility() && ItemList.searchItem(item.getAbility()!!) != null){
                ItemList.searchItem(item.getAbility()!!)!!.held(e)
            }
        }
    }
}