package net.mcmxsg.survivalcore.core.command

import net.bxx2004.pandalib.bukkit.commands.BukkitCommand
import net.bxx2004.pandalib.bukkit.commands.BukkitSubCommand
import net.bxx2004.pandalib.bukkit.commands.PCommand
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.clearLock
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.writeLock
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@BukkitCommand(name = "chest", permission = "chest.op", aliases = ["schest"])
class ChestCommand : PCommand(){
    override fun run(sender: CommandSender?, c: Command?, s: String?, args: Array<String>?): Boolean {
        commandHelp(null)
        return false
    }
    companion object{
        @JvmStatic
        @BukkitSubCommand(mainCommand = "chest", usage = "set", permission = "chest.op", description = "设置眼前箱子为锁")
        fun set(sender: CommandSender,args: Array<String>?) : Boolean{
            val player = sender as Player
            val block = player.getTargetBlockExact(3)!!
            if (block.type.toString().endsWith("CHEST")){
                block.writeLock(true)
            }
            return true
        }
        @JvmStatic
        @BukkitSubCommand(mainCommand = "chest", usage = "clear", permission = "chest.op", description = "设置眼前箱子普通")
        fun clear(sender: CommandSender,args: Array<String>?) : Boolean{
            val player = sender as Player
            val block = player.getTargetBlockExact(3)!!
            if (block.type.toString().endsWith("CHEST")){
                block.clearLock()
            }
            return true
        }
    }
}