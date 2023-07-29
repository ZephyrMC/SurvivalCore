package net.mcmxsg.survivalcore.util.intofunction

import de.tr7zw.changeme.nbtapi.NBTBlock
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import net.mcmxsg.survivalcore.core.api.core.data.BlockData
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.getMachine
import org.bukkit.block.Block

object BlockFunction {
    fun Block.isLockChest(): Boolean{
        var nbt = NBTBlock(this)
        if (nbt.data.hasTag("survivalcore")){
            if (nbt.data.getCompound("survivalcore").hasTag("chest")){
                if (nbt.data.getCompound("survivalcore").getBoolean("chest")){
                    return true
                }
            }
        }
        return false
    }
    fun Block.hasChest(): Boolean{
        var nbt = NBTBlock(this)
        if (nbt.data.hasTag("survivalcore")){
            if (nbt.data.getCompound("survivalcore").hasTag("chest")){
                return true
            }
        }
        return false
    }
    fun Block.writeLock(lock:Boolean){
        var nbt = NBTBlock(this)
        if (nbt.data.hasTag("survivalcore")){

        }else{
            nbt.data.addCompound("survivalcore")
        }
        val nbtc = nbt.data.getCompound("survivalcore")
        nbtc.setBoolean("chest",lock)
        nbt.data.mergeCompound(nbtc)
    }
    fun Block.clearLock(){
        var nbt = NBTBlock(this)
        if (nbt.data.hasTag("survivalcore")){

        }else{
            nbt.data.addCompound("survivalcore")
        }
        val nbtc = nbt.data.getCompound("survivalcore")
        nbtc.removeKey("chest")
        nbt.data.mergeCompound(nbtc)
    }
    fun Block.writeMachine(name: String){
        var nbt = NBTBlock(this)
        if (nbt.data.hasTag("survivalcore")){

        }else{
            nbt.data.addCompound("survivalcore")
        }
        val nbtc = nbt.data.getCompound("survivalcore")
        nbtc.setString("machine",name)
        nbt.data.mergeCompound(nbtc)
    }
    fun Block.clearMachine(){
        var nbt = NBTBlock(this)
        nbt.data.clearNBT()
    }
    fun Block.hasMachine(): Boolean{
        var nbt = NBTBlock(this)
        if (nbt.data.hasTag("survivalcore")){
            if (nbt.data.getCompound("survivalcore").hasTag("machine")){
                return true
            }
        }
        return false
    }
    fun Block.getMachine(): String?{
        var nbt = NBTBlock(this)
        if (nbt.data.hasTag("survivalcore")){
            if (nbt.data.getCompound("survivalcore").hasTag("machine")){
                return nbt.data.getCompound("survivalcore").getString("machine")
            }
        }
        return null
    }
    fun Block.writeData(data:BlockData<ABlockMachine>){
        var nbt = NBTBlock(this)
        nbt.data.mergeCompound(data.node())
    }
}