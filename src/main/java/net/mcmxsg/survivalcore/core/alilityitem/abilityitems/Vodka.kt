package net.mcmxsg.survivalcore.core.alilityitem.abilityitems

import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.core.Craftable
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Vodka : AAbilityItem(),Craftable<AAbilityItem>{
    override fun temple(): PItemStack {
        return PItemStack(Material.POTION,"&9伏特加","","&7香浓可口,回味无穷")
    }

    override fun interact(e: PlayerInteractEvent) {

    }

    override fun f(e: PlayerUseFEvent) {
        PTitle.To(e.player,"&c该物品无法交互")
    }

    override fun held(e: PlayerItemHeldEvent) {

    }

    override fun consume(e: PlayerItemConsumeEvent) {
        PTitle.To(e.player,"&c晕乎乎")
        e.player.addPotionEffect (PotionEffect(PotionEffectType.CONFUSION,600,3))
        e.player.addPotionEffect (PotionEffect(PotionEffectType.LEVITATION,60,3))
    }

    override fun name(): String {
        return "伏特加"
    }

    override fun material(): ArrayList<PItemStack> {
        var array = ArrayList<PItemStack>()
        array.add(PItemStack(Material.WATER_BUCKET))
        array.add(PItemStack(Material.FERMENTED_SPIDER_EYE))
        return array
    }

    override fun time(): Int {
        return 10
    }

    override fun chance(player: Player, item: AAbilityItem): Boolean {
        return true
    }

    override fun require(player: Player): Boolean {
        return true
    }
}