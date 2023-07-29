package net.mcmxsg.survivalcore.data

import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import net.mcmxsg.survivalcore.core.machine.machines.*

object MachineList {
    private val hooks : HashMap<String,ArrayList<ABlockMachine>> = HashMap<String,ArrayList<ABlockMachine>>()
    val UPGRADE_WORKBENCH: UpgradeWorkbench = UpgradeWorkbench()
    val TRASH: Trash = Trash()
    val AREA_CORE: AreaCore = AreaCore()
    val TIP : TipBlock = TipBlock()
    val CRAFT_TABLE : CraftTable = CraftTable()
    val FORGE_TABLE : ForgeTable = ForgeTable()

    fun register(plugin: String, item: ABlockMachine){
        if (!hooks.containsKey(plugin)){
            hooks[plugin] = ArrayList<ABlockMachine>()
        }
        var list = hooks[plugin]
        list!!.add(item)
        hooks[plugin] = list
    }
    fun searchMachine(name: String) : ABlockMachine?{
        MachineList.allMachine().forEach{
            if (it.name() == name){
                return it
            }
        }
        return null
    }
    fun get(plugin: String, name:String): ABlockMachine {
        return hooks[plugin]!!.filter { name != it.name() }[0]
    }
    fun get(plugin:String) : ArrayList<ABlockMachine> {
        return hooks[plugin]!!
    }
    fun getAll() : ArrayList<ABlockMachine>{
        var list = ArrayList<ABlockMachine>()
        hooks!!.values.forEach{
            it.forEach{
                list.add(it)
            }
        }
        return list
    }
    fun allMachine() : ArrayList<ABlockMachine>{
        var ilist = ArrayList<ABlockMachine>()
        MachineList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map { it.isAccessible = true; it.get(null) as ABlockMachine }.forEach {
            ilist.add(it)
        }
        ilist.addAll(MachineList.getAll())
        return ilist
    }
}