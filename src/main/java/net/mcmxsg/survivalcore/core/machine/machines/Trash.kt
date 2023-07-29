package net.mcmxsg.survivalcore.core.machine.machines

import net.bxx2004.pandalib.bukkit.gui.predefine.CommonMenu
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import net.mcmxsg.survivalcore.core.api.event.ItemAccessEvent
import net.mcmxsg.survivalcore.util.intofunction.PlayerFunction.targetMachine
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class Trash : ABlockMachine() {
    var values :HashMap<String,CommonMenu> = HashMap<String,CommonMenu>()
    override fun name(): String {
        return "垃圾桶"
    }

    override fun temple(): PItemStack {
        return PItemStack(Material.COMPOSTER,"&7垃圾桶"," ","&7建议的废物储存装置","&c储存的废物每天自动消除","&9Shift + 左键 &7切换便捷丢取模式","&9F &7与其交互")
    }

    override fun interact(e: PlayerInteractEvent) {
        if (e.action == Action.LEFT_CLICK_BLOCK && e.player.isSneaking){
            data(e.clickedBlock!!).setBoolean("isopen",!data(e.clickedBlock!!).getBoolean("isopen"))
            if (data(e.clickedBlock!!).getBoolean("isopen")){
                PTitle.To(e.player,"&a垃圾桶已开启")
            }else{
                PTitle.To(e.player,"&c垃圾桶已关闭")
            }
        }
    }

    override fun accessItem(e: ItemAccessEvent) {
        if (data(e.block!!).getBoolean("isopen")){
            if (values[e.block.location.toString()]!!.inventory().firstEmpty() != -1){
                values[e.block.location.toString()]!!.inventory().addItem(e.item.itemStack)
                e.item.remove()
            }
        }
    }
    override fun blockPlace(e: BlockPlaceEvent) :BlockPlaceEvent{
        data(e.block).setBoolean("isopen",false)
        return e
    }
    override fun blockBreak(e: BlockBreakEvent):BlockBreakEvent {
        try {
            values[e.block.location.toString()]!!.inventory().contents!!.filterNotNull().forEach(){ a ->
                e.block.world.dropItem(e.block.location,a)
            }
            values.remove(e.block.location.toString())
        }catch (e:Exception){}
        return e
    }
    override fun f(e: PlayerUseFEvent) {
        if (!values.containsKey(e.player.targetMachine().location.toString())){
            values.put(e.player.targetMachine().location.toString(),CommonMenu("垃圾桶",27))
        }
        values.get(e.player.targetMachine().location.toString())!!.open(e.player)
    }

    override fun onBlock(block: Block,pplayer: Player) {

    }
}