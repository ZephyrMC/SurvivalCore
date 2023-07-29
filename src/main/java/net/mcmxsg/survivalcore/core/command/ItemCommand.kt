package net.mcmxsg.survivalcore.core.command

import net.bxx2004.pandalib.bukkit.commands.BukkitCommand
import net.bxx2004.pandalib.bukkit.commands.BukkitSubCommand
import net.bxx2004.pandalib.bukkit.commands.PCommand
import net.bxx2004.pandalib.bukkit.util.PPlugin
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.data.ItemList
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@BukkitCommand(name = "itemstack", permission = "machine.op", aliases = ["sitem"])
class ItemCommand : PCommand(){
    override fun run(sender: CommandSender?, c: Command?, s: String?, args: Array<String>?): Boolean {
        commandHelp(null)
        return false
    }

    override fun onTabComplete(
        commandSender: CommandSender,
        command: Command,
        s: String,
        strings: Array<out String>
    ): MutableList<String>? {
        if (strings[0].equals("get")){
            val a : ArrayList<String> = ArrayList<String>()
            ItemList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map { it.isAccessible = true; it.get(null) as AAbilityItem }.forEach {
                a.add(it.name())
            }
            return a
        }
        return null
    }
    companion object{
        @JvmStatic
        @BukkitSubCommand(mainCommand = "itemstack", usage = "get <物品>", permission = "chest.op", description = "获取一个功能性物品")
        fun get(sender: CommandSender, args: Array<String>?) : Boolean{
            val player = sender as Player
            val item: AAbilityItem = ItemList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map {it.isAccessible = true; it.get(null) as AAbilityItem }.filter { it.name().equals(
                args?.get(1)
            ) }[0]
            player.inventory.addItem(item.asItem())
            return true
        }
        @JvmStatic
        @BukkitSubCommand(mainCommand = "itemstack", usage = "getother <插件> <物品>", permission = "chest.op", description = "获取一个功能性物品")
        fun getother(sender: CommandSender, args: Array<String>?) : Boolean{
            val player = sender as Player
            val item: AAbilityItem = ItemList.get(args!![1],args[2])
            player.inventory.addItem(item.asItem())
            return true
        }
    }
}