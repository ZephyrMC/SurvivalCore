package net.mcmxsg.survivalcore.core.api.core

import de.tr7zw.changeme.nbtapi.NBTCompound
import de.tr7zw.changeme.nbtapi.NBTItem
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.mcmxsg.survivalcore.core.api.core.data.BlockData
import net.mcmxsg.survivalcore.core.api.core.data.ItemData
import net.mcmxsg.survivalcore.data.ItemList
import net.mcmxsg.survivalcore.data.MachineList
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer
import java.util.function.Function

interface Upgradable<T> {
    fun up(nowItem:PItemStack) : PItemStack
    fun canUp(nowItem: PItemStack) : Boolean
    fun max() : Int
    fun level(nowItem: PItemStack) : Int
    fun levelTag() : Array<String>
    fun type() : Type
    fun material(nextlevel: Int) : Array<ItemStack>
    enum class Type{
        MACHINE,
        ABILITY
    }
    object UpgradableConstValue{
        val CAN_LEVELTAG = "&9<可升级>"
        val MAX_TAG = "&c<已满级>"
        fun LEVEL(s:String) : String{
            return "&9当前等级:&r $s"
        }
    }
    companion object{
        fun isUpgradableAbility(item: PItemStack) : Boolean{
            var nbtItem = NBTItem(item)
            if (!nbtItem.hasTag("survivalcore")){
                return false
            }
            if (!nbtItem.getCompound("survivalcore").hasTag("abilitydata")){
                return false
            }
            if (!nbtItem.getCompound("survivalcore").getCompound("abilitydata").hasTag("upgradable")){
                return false
            }
            return true
        }
        fun isUpgradableMachine(item: PItemStack) : Boolean{
            var nbtItem = NBTItem(item)
            if (!nbtItem.hasTag("survivalcore")){
                return false
            }
            if (!nbtItem.getCompound("survivalcore").hasTag("machinedata")){
                return false
            }
            if (!nbtItem.getCompound("survivalcore").getCompound("machinedata").hasTag("upgradable")){
                return false
            }
            return true
        }
        fun read(type:String,item: PItemStack,consumer: Consumer<Upgradable<*>>){
            var nbtItem = NBTItem(item)
            if (!nbtItem.hasTag("survivalcore")){
                nbtItem.addCompound("survivalcore")
            }
            if (!nbtItem.getCompound("survivalcore").hasTag("${type}data")){
                nbtItem.getCompound("survivalcore").addCompound("${type}data")
            }
            if (!nbtItem.getCompound("survivalcore").getCompound("${type}data").hasTag("upgradable")){
                nbtItem.getCompound("survivalcore").getCompound("${type}data").addCompound("upgradable")
            }
            var nbt = nbtItem.getCompound("survivalcore").getCompound("${type}data").getCompound("upgradable")
            var up = object : Upgradable<AAbilityItem> {

                override fun canUp(nowItem: PItemStack): Boolean {
                    return nbt.getBoolean("canUp")
                }

                override fun max(): Int {
                    return nbt.getInteger("max")
                }


                override fun levelTag(): Array<String> {
                    return nbt.getString("levelTag").split("-").toTypedArray()
                }

                override fun type(): Type {
                    return Type.valueOf(nbt.getString("type"))
                }

                override fun material(nextlevel: Int): Array<ItemStack> {
                    TODO("Not yet implemented")
                }

                override fun up(nowItem: PItemStack): PItemStack {
                    TODO("Not yet implemented")
                }

                override fun level(nowItem: PItemStack): Int {
                    TODO("Not yet implemented")
                }

            }
            consumer.accept(up)
        }
        fun defaultItemLevel(item: PItemStack,level: Int) : PItemStack{
            var nbtItem = NBTItem(item)
            if (!nbtItem.hasTag("survivalcore")){
                nbtItem.addCompound("survivalcore")
            }
            if (!nbtItem.getCompound("survivalcore").hasTag("abilitydata")){
                nbtItem.getCompound("survivalcore").addCompound("abilitydata")
            }
            if (!nbtItem.getCompound("survivalcore").getCompound("abilitydata").hasTag("upgradable")){
                nbtItem.getCompound("survivalcore").getCompound("abilitydata").addCompound("upgradable")
            }
            var nbt = nbtItem.getCompound("survivalcore").getCompound("abilitydata").getCompound("upgradable")
            nbt.setInteger("level",level)
            nbtItem.mergeCompound(nbt)
            return PItemStack(nbtItem.item)
        }
        fun readItemLevel(item: PItemStack) : Int{
            var nbtItem = NBTItem(item)
            if (!nbtItem.hasTag("survivalcore")){
                nbtItem.addCompound("survivalcore")
            }
            if (!nbtItem.getCompound("survivalcore").hasTag("abilitydata")){
                nbtItem.getCompound("survivalcore").addCompound("abilitydata")
            }
            if (!nbtItem.getCompound("survivalcore").getCompound("abilitydata").hasTag("upgradable")){
                nbtItem.getCompound("survivalcore").getCompound("abilitydata").addCompound("upgradable")
            }
            var nbt = nbtItem.getCompound("survivalcore").getCompound("abilitydata").getCompound("upgradable")
            return nbt.getInteger("level")
        }
        fun defaultItem(item:PItemStack,up: Upgradable<AAbilityItem>) : PItemStack{
            var nbtItem = NBTItem(item)
            if (!nbtItem.hasTag("survivalcore")){
                nbtItem.addCompound("survivalcore")
            }
            if (!nbtItem.getCompound("survivalcore").hasTag("abilitydata")){
                nbtItem.getCompound("survivalcore").addCompound("abilitydata")
            }
            if (!nbtItem.getCompound("survivalcore").getCompound("abilitydata").hasTag("upgradable")){
                nbtItem.getCompound("survivalcore").getCompound("abilitydata").addCompound("upgradable")
            }
            var nbt = nbtItem.getCompound("survivalcore").getCompound("abilitydata").getCompound("upgradable")
            nbt.setInteger("level",0)
            nbt.setString("type",Type.ABILITY.name)
            nbtItem.mergeCompound(nbt)
            return PItemStack(nbtItem.item)
        }
        fun defaultMachine(item:PItemStack,up: Upgradable<ABlockMachine>) : PItemStack{
            var nbtItem = NBTItem(item)
            if (!nbtItem.hasTag("survivalcore")){
                nbtItem.addCompound("survivalcore")
            }
            if (!nbtItem.getCompound("survivalcore").hasTag("machinedata")){
                nbtItem.getCompound("survivalcore").addCompound("machinedata")
            }
            if (!nbtItem.getCompound("survivalcore").getCompound("machinedata").hasTag("upgradable")){
                nbtItem.getCompound("survivalcore").getCompound("machinedata").addCompound("upgradable")
            }
            var nbt = nbtItem.getCompound("survivalcore").getCompound("machinedata").getCompound("upgradable")
            nbt.setBoolean("canUp",up.canUp(item))
            nbt.setInteger("max",up.max())
            nbt.setInteger("level",up.level(item))
            var s = ""
            up.levelTag().forEach {
                s += "$it-"
            }
            s = s.substring(0,s.length)
            nbt.setString("levelTag",s)
            nbt.setString("type",Type.ABILITY.name)
            nbtItem.mergeCompound(nbt)
            return PItemStack(nbtItem.item)
        }
        fun searchItem(name: String) : AAbilityItem?{
            ItemList.allItem().forEach{
                if (it.name() == name){
                    return it
                }
            }
            return null
        }
        fun searchMachine(name: String) : ABlockMachine?{
            MachineList.allMachine().forEach{
                if (it.name() == name){
                    return it
                }
            }
            return null
        }
        fun getName(item: PItemStack) : String{
            var nbtItem = NBTItem(item)
            return if (nbtItem.getCompound("survivalcore").hasTag("machine")){
                nbtItem.getCompound("survivalcore").getString("machine")
            }else{
                nbtItem.getCompound("survivalcore").getString("ability")
            }
        }
        fun getType(item: PItemStack) : Type{
            var nbtItem = NBTItem(item)
            return if (nbtItem.getCompound("survivalcore").hasTag("machine")){
                return Type.MACHINE
            }else{
                return Type.ABILITY
            }
        }
        fun writeItemLevel(item: PItemStack,level: Int): PItemStack{
            var nbtItem = NBTItem(item)
            if (!nbtItem.hasTag("survivalcore")){
                nbtItem.addCompound("survivalcore")
            }
            if (!nbtItem.getCompound("survivalcore").hasTag("abilitydata")){
                nbtItem.getCompound("survivalcore").addCompound("abilitydata")
            }
            if (!nbtItem.getCompound("survivalcore").getCompound("abilitydata").hasTag("upgradable")){
                nbtItem.getCompound("survivalcore").getCompound("abilitydata").addCompound("upgradable")
            }
            nbtItem.getCompound("survivalcore").getCompound("abilitydata").getCompound("upgradable").setInteger("level",level)
            return PItemStack(nbtItem.item)
        }
        fun writeMachineLevel(item: PItemStack,level: Int) : PItemStack{
            var nbtItem = NBTItem(item)
            if (!nbtItem.hasTag("survivalcore")){
                nbtItem.addCompound("survivalcore")
            }
            if (!nbtItem.getCompound("survivalcore").hasTag("machinedata")){
                nbtItem.getCompound("survivalcore").addCompound("machinedata")
            }
            if (!nbtItem.getCompound("survivalcore").getCompound("machinedata").hasTag("upgradable")){
                nbtItem.getCompound("survivalcore").getCompound("machinedata").addCompound("upgradable")
            }
            nbtItem.getCompound("survivalcore").getCompound("machinedata").getCompound("upgradable").setInteger("level",level)
            return PItemStack(nbtItem.item)
        }
    }
}