package net.mcmxsg.survivalcore.core.api.core.data

import de.tr7zw.changeme.nbtapi.NBTCompound
import de.tr7zw.changeme.nbtapi.NBTItem
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import java.util.function.Function

class BlockData<T : ABlockMachine>(objectdata : T) {
    private var t : T = objectdata
    private var item : PItemStack = t.asItem()
    fun asItem(): T{
        return t
    }
    fun node() : NBTCompound{
        var nbtItem = NBTItem(item)
        return nbtItem.getCompound("survivalcore").getCompound("machinedata")
    }
    fun write(consumer : Function<NBTCompound, NBTCompound>){
        var nbtItem = NBTItem(item)
        if (!nbtItem.hasTag("survivalcore")){
            nbtItem.addCompound("survivalcore")
        }
        if (!nbtItem.getCompound("survivalcore").hasTag("machinedata")){
            nbtItem.getCompound("survivalcore").addCompound("machinedata")
        }
        var nbt = consumer.apply(nbtItem.getCompound("survivalcore").getCompound("machinedata"))
        nbtItem.mergeCompound(nbt)
        item = PItemStack(nbtItem.item)
    }
    fun<Type> read(consumer: Function<NBTCompound, Type>) : Type{
        var nbtItem = NBTItem(item)
        if (!nbtItem.hasTag("survivalcore")){
            nbtItem.addCompound("survivalcore")
        }
        if (!nbtItem.getCompound("survivalcore").hasTag("machinedata")){
            nbtItem.getCompound("survivalcore").addCompound("machinedata")
        }
        return consumer.apply(nbtItem.getCompound("survivalcore").getCompound("machinedata"))
    }
}