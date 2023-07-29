package net.mcmxsg.survivalcore.core.machine.machines

import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.bxx2004.pandalib.bukkit.task.Task
import net.bxx2004.pandalib.bukkit.util.PPlugin
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.BiConsumer
import java.util.function.Consumer

class TipBlock : ABlockMachine(){
    override fun onBlock(block: Block,player: Player) {
        PTitle.ToWithWrite(PTitle.PTitleType.SUB_TITLE,2,player,data(block).getString("tip").split("-")[0])
        object :BukkitRunnable(){
            override fun run() {
                try {
                    player.playSound(player,Sound.UI_BUTTON_CLICK,10.0F,10.0F)
                    PTitle.To(player,data(block).getString("tip").split("-")[1] + "&nbsp" + data(block).getString("tip").split("-")[0])
                }catch (e: Exception){}
            }
        }.runTaskLater(PPlugin.getPlugin("SurvivalCore"), (data(block).getString("tip").length * 2).toLong())
    }
    override fun name(): String {
        return "提醒方块"
    }

    override fun blockPlace(e: BlockPlaceEvent) :BlockPlaceEvent{
        data(e.block).setString("tip","空内容")
        return e
    }
    override fun temple(): PItemStack {
        return PItemStack(Material.REDSTONE_BLOCK,"&7提醒方块"," ","&7站上去时提醒标题")
    }
}