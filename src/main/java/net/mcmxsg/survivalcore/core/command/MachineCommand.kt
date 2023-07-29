package net.mcmxsg.survivalcore.core.command

import de.tr7zw.changeme.nbtapi.NBTBlock
import net.bxx2004.pandalib.bukkit.commands.BukkitCommand
import net.bxx2004.pandalib.bukkit.commands.BukkitSubCommand
import net.bxx2004.pandalib.bukkit.commands.PCommand
import net.bxx2004.pandalib.bukkit.util.PPlugin
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import net.mcmxsg.survivalcore.core.machine.machines.AreaCore
import net.mcmxsg.survivalcore.data.ItemList
import net.mcmxsg.survivalcore.data.MachineList
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
@BukkitCommand(name = "machine", permission = "machine.op", aliases = ["smachine"])
class MachineCommand : PCommand(){
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
            MachineList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map { it.isAccessible = true; it.get(null) as ABlockMachine }.forEach {
                a.add(it.name())
            }
            return a
        }
        return null
    }
    companion object{
        @JvmStatic
        @BukkitSubCommand(mainCommand = "machine", usage = "get <机器>", permission = "chest.op", description = "获取一个机器")
        fun get(sender: CommandSender, args: Array<String>?) : Boolean{
            val player = sender as Player
            val item: ABlockMachine = MachineList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map {it.isAccessible = true; it.get(null) as ABlockMachine }.filter { it.name().equals(
                args?.get(1)
            ) }[0]
            player.inventory.addItem(item.asItem())
            return true
        }
        @JvmStatic
        @BukkitSubCommand(mainCommand = "machine", usage = "opengui", permission = "chest.op", description = "打开营地GUI")
        fun opengui(sender: CommandSender, args: Array<String>?) : Boolean{
            val player = sender as Player
            AreaCore.AreaGetGUI().open(player)
            return true
        }
        @JvmStatic
        @BukkitSubCommand(mainCommand = "machine", usage = "clearall", permission = "chest.op", description = "清除眼前的机器数据")
        fun clear(sender: CommandSender, args: Array<String>?) : Boolean{
            val player = sender as Player
            NBTBlock(player.getTargetBlockExact(3)!!).data.removeKey("survivalcore")
            return true
        }
        @JvmStatic
        @BukkitSubCommand(mainCommand = "machine", usage = "getother <插件> <机器>", permission = "chest.op", description = "获取一个功能性物品")
        fun getother(sender: CommandSender, args: Array<String>?) : Boolean{
            val player = sender as Player
            val item: ABlockMachine = MachineList.get(args!![1],args[2])
            player.inventory.addItem(item.asItem())
            return true
        }
    }
}