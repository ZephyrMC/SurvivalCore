package net.mcmxsg.survivalcore.util.intofunction

import de.tr7zw.changeme.nbtapi.NBTBlock
import de.tr7zw.changeme.nbtapi.NBTItem
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import net.mcmxsg.survivalcore.core.api.core.Upgradable
import net.mcmxsg.survivalcore.core.api.core.data.BlockData
import net.mcmxsg.survivalcore.core.api.core.data.ItemData
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.getAbility
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import kotlin.math.PI

object ItemStackFunction {
    fun ItemStack.writeMachine(s:String) : PItemStack{
        val nbitem = NBTItem(this)
        if (nbitem.hasTag("survivalcore")){

        }else{
            nbitem.addCompound("survivalcore")
        }
        val nbtc = nbitem.getCompound("survivalcore")
        nbtc.setString("machine",s)
        nbitem.mergeCompound(nbtc)
        return PItemStack(nbitem.item)
    }
    fun ItemStack.hasMachine() : Boolean{
        val nbitem = NBTItem(this)
        if (nbitem.hasTag("survivalcore")){
            if (nbitem.getCompound("survivalcore").hasTag("machine")){
                return true
            }
        }
        return false
    }
    fun ItemStack.getMachine() : String?{
        val nbitem = NBTItem(this)
        if (nbitem.hasTag("survivalcore")){
            return nbitem.getCompound("survivalcore").getString("machine")
        }
        return null
    }
    fun ItemStack.writeAbility(s:String) : ItemStack {
        val nbitem = NBTItem(this)
        if (nbitem.hasTag("survivalcore")){

        }else{
            nbitem.addCompound("survivalcore")
        }
        val nbtc = nbitem.getCompound("survivalcore")
        nbtc.setString("ability",s)
        nbitem.mergeCompound(nbtc)
        return nbitem.item
    }
    fun ItemStack.hasAbility() : Boolean{
        val nbitem = NBTItem(this)
        if (nbitem.hasTag("survivalcore")){
            if (nbitem.getCompound("survivalcore").hasTag("ability")){
                return true
            }
        }
        return false
    }
    fun ItemStack.getAbility() : String?{
        val nbitem = NBTItem(this)
        if (nbitem.hasTag("survivalcore")){
            return nbitem.getCompound("survivalcore").getString("ability")
        }
        return null
    }
    fun ItemStack.writeData(data: ItemData<AAbilityItem>) : ItemStack{
        var nbt = NBTItem(this)
        nbt.mergeCompound(data.node())
        return nbt.item
    }
}