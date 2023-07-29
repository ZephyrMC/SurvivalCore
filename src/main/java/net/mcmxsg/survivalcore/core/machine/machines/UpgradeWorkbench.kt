package net.mcmxsg.survivalcore.core.machine.machines

import net.bxx2004.pandalib.bukkit.gui.predefine.StructuralMenu
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import net.mcmxsg.survivalcore.core.api.core.Upgradable
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerInteractEvent


class UpgradeWorkbench : ABlockMachine() {
    override fun name(): String {
        return "升级工作台"
    }

    override fun temple(): PItemStack {
        return PItemStack(Material.IRON_BLOCK,"&7升级工作台","&7用于物品升级")
    }

    override fun interact(e: PlayerInteractEvent) {
        if (e.action == Action.RIGHT_CLICK_BLOCK){
            e.isCancelled = true
            PTitle.To(e.player,"&c请使用 &7&LF &c交互")
        }
    }
    override fun f(e: PlayerUseFEvent) {
        Menu().open(e.player)
    }
    class Menu : StructuralMenu("升级工作台",
    "*********",
           "***a*b***",
           "****c****",
           "111111111",
           "111111111",
           "111111111"
    ){
        fun inThis(c:Int,a:Int,b:Int): Boolean{
            for (i in a until b+1){
                if (c == i){
                    return true
                }
            }
            return false
        }
        override fun open(event: InventoryOpenEvent?) {
            setItem('*', PItemStack(Material.BLACK_STAINED_GLASS_PANE))
            setItem('a', PItemStack(Material.GREEN_STAINED_GLASS,"&7拖动物品放在这里"))
            setItem('b', PItemStack(Material.GREEN_STAINED_GLASS,"&7这里将显示升级结果"))
            setItem('c', PItemStack(Material.ANVIL,"&7下面显示所需材料"))
        }

        override fun close(event: InventoryCloseEvent) {
            try {
                if (event.inventory.getItem(12) != null && event.inventory.getItem(12)!!.type != Material.AIR && event.inventory.getItem(12)!!.type != Material.GREEN_STAINED_GLASS){
                    var cursor = PItemStack(event.inventory.getItem(12))
                    if (Upgradable.isUpgradableAbility(cursor) || Upgradable.isUpgradableMachine(cursor)){
                        if (Upgradable.getType(cursor) == Upgradable.Type.MACHINE){
                            var up = Upgradable.searchMachine(Upgradable.getName(cursor))!! as Upgradable<ABlockMachine>
                            for (i in 27 until 53){
                                try {
                                    if (event.inventory.getItem(i)!!.type == Material.BLUE_STAINED_GLASS_PANE){
                                        event.player.inventory.addItem(up.material(up.level(cursor) + 1)[i-27])
                                    }
                                }catch (e: Exception){}
                            }
                        }else{
                            var up = Upgradable.searchItem(Upgradable.getName(cursor))!! as Upgradable<AAbilityItem>
                            for (i in 27 until 53){
                                try {
                                    if (event.inventory.getItem(i)!!.type == Material.BLUE_STAINED_GLASS_PANE){
                                        event.player.inventory.addItem(up.material(up.level(cursor) + 1)[i-27])
                                    }
                                }catch (e: Exception){}
                            }
                        }
                        event.player.inventory.addItem(cursor)
                    }
                }
            }catch (e:Exception){e.printStackTrace()}
        }
        override fun click(event: InventoryClickEvent) {
            if (inThis(event.rawSlot,0,54)){
                event.isCancelled = true
            }
            when(event.rawSlot){
                12 -> {
                    if (event.cursor != null && event.cursor!!.type != Material.AIR){
                        var cursor = PItemStack(event.cursor)
                        if (Upgradable.isUpgradableAbility(cursor) || Upgradable.isUpgradableMachine(cursor)){
                            setItem('a',PItemStack(event.cursor))
                            event.cursor = null
                            if (Upgradable.getType(cursor) == Upgradable.Type.MACHINE){
                                var up = Upgradable.searchMachine(Upgradable.getName(cursor))!! as Upgradable<ABlockMachine>
                                for (i in 27 until 53){
                                    try {
                                        inventory().setItem(i,up.material(up.level(cursor)+1)[i-27])
                                    }catch (e: Exception){}
                                }
                                setItem('b',up.up(cursor))
                            }else{
                                var up = Upgradable.searchItem(Upgradable.getName(cursor))!! as Upgradable<AAbilityItem>
                                for (i in 27 until 53){
                                    try {
                                        inventory().setItem(i,up.material(up.level(cursor)+1)[i-27])
                                    }catch (e: Exception){}
                                }
                                setItem('b',up.up(cursor))
                            }
                        }
                    }
                }
                14 -> {
                    var click = PItemStack(event.currentItem)
                    if (event.inventory.getItem(12)!!.type != Material.GREEN_STAINED_GLASS){
                        if (isResult()){
                            event.whoClicked.inventory.addItem(click)
                            setItem('*', PItemStack(Material.BLACK_STAINED_GLASS_PANE))
                            setItem('a', PItemStack(Material.GREEN_STAINED_GLASS,"&7拖动物品放在这里"))
                            setItem('b', PItemStack(Material.GREEN_STAINED_GLASS,"&7这里将显示升级结果"))
                            setItem('c', PItemStack(Material.ANVIL,"&7下面显示所需材料"))
                            setItem('1',null)
                        }
                    }
                }
                else -> {
                    if (inThis(event.rawSlot,20,55)){
                        if (event.cursor != null && event.currentItem != null){
                            var cursor = PItemStack(event.cursor)
                            var now = PItemStack(event.currentItem)
                            if (cursor.equals(now)){
                                inventory().setItem(event.rawSlot,PItemStack(Material.BLUE_STAINED_GLASS_PANE,"&a已提交"))
                                event.cursor = null
                            }
                        }
                    }
                }
            }
        }
        private fun isResult() : Boolean{
            for (i in 27 until 53){
                if (inventory().getItem(i) != null && inventory().getItem(i)!!.type != Material.BLUE_STAINED_GLASS_PANE){
                    return false
                }
            }
            return true
        }
    }
}