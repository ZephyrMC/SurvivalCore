package net.mcmxsg.survivalcore.core.interact

import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.bxx2004.pandalib.bukkit.language.abandon.PMessage
import net.bxx2004.pandalib.bukkit.listener.BukkitListener
import net.bxx2004.pandalib.bukkit.util.PMath
import net.bxx2004.pandalib.bukkit.util.PPlugin
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.data.Config
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.hasChest
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.isLockChest
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.writeLock
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable

@BukkitListener()
class IChest : Listener {
    @EventHandler
    fun onRightChest(event: PlayerInteractEvent){
        if (event.action == Action.RIGHT_CLICK_BLOCK){
            if (event.clickedBlock!!.type.toString().endsWith("CHEST")){
                if (event.clickedBlock!!.isLockChest()){
                    PMessage.to(event.player,Config.prefix + "&7箱子已锁,请按F撬开箱子...")
                    event.isCancelled = true
                    event.clickedBlock
                }
            }
        }
    }
    @EventHandler
    fun onBreakChest(e:BlockBreakEvent){
        val block = e.block
        if (block.hasChest()){
            e.isCancelled = true
            val player = e.player
            PTitle.To(player,"&7坚固的铁箱,无法挖掘")
        }
    }
    @EventHandler
    fun onInteractChest(e: PlayerUseFEvent){
        val player = e.player
        if (player.getTargetBlockExact(3) != null && player.getTargetBlockExact(3)!!.type != Material.AIR){
            if (player.getTargetBlockExact(3)!!.type.toString().endsWith("CHEST")){
                e.isCancelled = true
                val block = player.getTargetBlockExact(3)!!
                if (block.isLockChest()){
                    PTitle.To(player,"&7正在撬锁")
                    var i: Int = 0
                    val x: Int = player.location.blockX
                    val z: Int = player.location.blockZ
                    object : BukkitRunnable(){
                        override fun run() {
                            PTitle.To(player,"&c撬锁中&nbsp&d&L[ |- " + PMath.progress(i,"=====") + " &d&l-| ]")
                            if (player.location.blockX != x || player.location.blockZ != z){
                                PTitle.To(player,"&7你已移动,无法进行撬锁工作")
                                cancel()
                            }
                            if (i >= 5){
                                PTitle.To(player,"&e锁已撬开")
                                block.writeLock(false)
                                cancel()
                            }
                            i++
                        }
                    }.runTaskTimerAsynchronously(PPlugin.getPlugin("SurvivalCore"),0,20)
                }else{
                    PTitle.To(player,"&7该箱子已无锁")
                }
            }
        }
    }
}
