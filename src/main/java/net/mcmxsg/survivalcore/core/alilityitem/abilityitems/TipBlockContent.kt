package net.mcmxsg.survivalcore.core.alilityitem.abilityitems

import de.tr7zw.changeme.nbtapi.NBTBlock
import de.tr7zw.changeme.nbtapi.NBTCompound
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.application.PConversation
import net.bxx2004.pandalib.bukkit.util.PPlugin
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.getMachine
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable

class TipBlockContent : AAbilityItem() {
    override fun temple(): PItemStack {
        return PItemStack(Material.STICK,"&7提醒方块内容修改器"," ","&7修改方块提醒的内容","&cF &7对准使用方块")
    }
    override fun f(e: PlayerUseFEvent) {
        var block = e.player.getTargetBlockExact(3)!!
        if (block == null || block.type == Material.AIR){
            return
        }
        if (block.getMachine() != "提醒方块"){
            return
        }
        var conservation = PConversation("&7请输入修改的名称(输入cancel取消)",false)
        conservation.start(e.player)
        object : BukkitRunnable(){
            override fun run() {
                if (conservation.answer() != null){
                    var name = conservation.answer()
                    data(block).setString("tip",name)
                    conservation.end(e.player)
                    cancel()
                }
            }
        }.runTaskTimerAsynchronously(PPlugin.getPlugin("SurvivalCore"),0,20)
    }

    override fun name(): String {
        return "提醒方块修改器"
    }
    fun data(block: Block) : NBTCompound {
        val nbt : NBTBlock = NBTBlock(block)
        if (!nbt.data.getCompound("survivalcore").hasTag("machinedata")){
            nbt.data.getCompound("survivalcore").addCompound("machinedata")
        }
        return nbt.data.getCompound("survivalcore").getCompound("machinedata")
    }
}