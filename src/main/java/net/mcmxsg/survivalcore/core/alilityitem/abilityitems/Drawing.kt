package net.mcmxsg.survivalcore.core.alilityitem.abilityitems

import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.core.BlockItem
import net.mcmxsg.survivalcore.core.api.core.Forgeable
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.data.ItemList
import org.bukkit.Material

class Drawing(name:String) : AAbilityItem() {
    var name:String
    init {
        this.name = name
    }
    override fun temple(): PItemStack {
        return PItemStack(Material.PAPER, "&7$name","&c此物品为锻造图纸","&c请配合锻造台使用")
    }
    override fun name(): String {
        return "锻造图纸"
    }

    override fun f(e: PlayerUseFEvent) {
        if (e.player.isOp){
            var list = ArrayList<BlockItem>()
            ItemList.allItem().forEach {
                if (it is Forgeable){
                    list.add(it)
                }
            }
            list.forEach {
                e.player.sendMessage(it.name())
                e.player.sendMessage("以上是可修改的锻造图纸,修改名就行了 例如: &7多功能工具")
            }
        }else{
            PTitle.To(e.player,"&c该物品无法交互")
        }
    }
}