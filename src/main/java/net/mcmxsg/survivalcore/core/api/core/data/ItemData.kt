package net.mcmxsg.survivalcore.core.api.core.data

import de.tr7zw.changeme.nbtapi.NBTCompound
import de.tr7zw.changeme.nbtapi.NBTItem
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import java.util.function.Function

class ItemData<T : AAbilityItem>(objectdata : T) {
    private var t : T = objectdata
    private var item : PItemStack = t.asItem()
    fun asItem(): T{
        return t
    }
    fun node() : NBTCompound{
        var nbtItem = NBTItem(item)
        return nbtItem.getCompound("survivalcore").getCompound("abilitydata")
    }
    fun write(consumer : Function<NBTCompound,NBTCompound>){
        var nbtItem = NBTItem(item)
        if (!nbtItem.hasTag("survivalcore")){
            nbtItem.addCompound("survivalcore")
        }
        if (!nbtItem.getCompound("survivalcore").hasTag("abilitydata")){
            nbtItem.getCompound("survivalcore").addCompound("abilitydata")
        }
        var nbt = consumer.apply(nbtItem.getCompound("survivalcore").getCompound("abilitydata"))
        nbtItem.mergeCompound(nbt)
        item = PItemStack(nbtItem.item)
    }
    fun<Type> read(consumer: Function<NBTCompound,Type>) : Type{
        var nbtItem = NBTItem(item)
        if (!nbtItem.hasTag("survivalcore")){
            nbtItem.addCompound("survivalcore")
        }
        if (!nbtItem.getCompound("survivalcore").hasTag("abilitydata")){
            nbtItem.getCompound("survivalcore").addCompound("abilitydata")
        }
        return consumer.apply(nbtItem.getCompound("survivalcore").getCompound("abilitydata"))
    }
}