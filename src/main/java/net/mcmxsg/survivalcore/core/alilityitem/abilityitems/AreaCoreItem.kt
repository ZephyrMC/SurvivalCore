package net.mcmxsg.survivalcore.core.alilityitem.abilityitems

import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerItemHeldEvent
class AreaCoreItem : AAbilityItem() {
    override fun temple(): PItemStack {
        return PItemStack(Material.REDSTONE,"&7破碎的营地核心")
    }

    override fun interact(e: PlayerInteractEvent) {

    }

    override fun f(e: PlayerUseFEvent) {
        PTitle.To(e.player,"&c该物品无法交互")
    }

    override fun held(e: PlayerItemHeldEvent) {

    }

    override fun consume(e: PlayerItemConsumeEvent) {

    }

    override fun name(): String {
        return "破碎的营地核心"
    }
}