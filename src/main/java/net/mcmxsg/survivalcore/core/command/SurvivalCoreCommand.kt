package net.mcmxsg.survivalcore.core.command

import net.bxx2004.pandalib.bukkit.commands.BukkitCommand
import net.bxx2004.pandalib.bukkit.commands.BukkitSubCommand
import net.bxx2004.pandalib.bukkit.commands.PCommand
import net.bxx2004.pandalib.bukkit.gui.predefine.FlipMenu
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.util.PMath
import net.bxx2004.pandalib.bukkit.util.PPlugin
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import net.mcmxsg.survivalcore.data.ItemList
import net.mcmxsg.survivalcore.data.MachineList
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent

@BukkitCommand(name = "survivalcore", permission = "machine.op", aliases = ["sv","score"])
class SurvivalCoreCommand : PCommand(){
    override fun run(p0: CommandSender?, p1: Command?, p2: String?, p3: Array<out String>?): Boolean {
        commandHelp(null)
        return false
    }
    companion object {
        @JvmStatic
        @BukkitSubCommand(mainCommand = "survivalcore", usage = "gui <物品|机器> <插件名>", permission = "gui.op", description = "打开指定列表")
        fun get(sender: CommandSender, args: Array<String>?): Boolean {
            val player = sender as Player
            var si = false
            if (args!!.size == 3){
                si = true
            }
            if (args[1] == "物品"){
                var ilist = ArrayList<AAbilityItem>()
                if (si){
                    ilist.addAll(ItemList.get(args[2]))
                }else{
                    ItemList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map { it.isAccessible = true; it.get(null) as AAbilityItem }.forEach {
                        ilist.add(it)
                    }
                }
                Gui(ilist.map { it.asItem() }).open(sender as Player)
            }else{
                var ilist = ArrayList<ABlockMachine>()
                if (si){
                    ilist.addAll(MachineList.get(args[2]))
                }else{
                    MachineList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map { it.isAccessible = true; it.get(null) as ABlockMachine }.forEach {
                        ilist.add(it)
                    }
                }
                Gui(ilist.map { it.asItem() }).open(sender as Player)
            }
            return true
        }
    }
    class Gui(items: List<PItemStack>) : FlipMenu("所有已注册的物品"){
        private var items : List<PItemStack>
        init {
            this.items = items
        }
        override fun click(event: InventoryClickEvent?) {
            if (event!!.currentItem != null && event.currentItem!!.type != Material.AIR){
                event!!.isCancelled = true
                var p = event.whoClicked as Player
                p.inventory.addItem(event.currentItem!!)
            }
            super.click(event)
        }
        override fun open(event: InventoryOpenEvent) {
            for (i in 0 until PMath.formatNumber(items.size, 35)) {
                val itemStack = arrayOfNulls<PItemStack>(35)
                for (o in 0..34) {
                    try {
                        itemStack[o] = PItemStack(items[o + i * 35])
                    } catch (e: Exception) {
                        break
                    }
                }
                setItem(i, *itemStack)
            }
            super.open(event)
        }
    }
}