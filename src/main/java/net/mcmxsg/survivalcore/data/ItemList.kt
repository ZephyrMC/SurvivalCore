package net.mcmxsg.survivalcore.data

import net.mcmxsg.survivalcore.core.alilityitem.abilityitems.*
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine

object ItemList {
    private val hooks : HashMap<String,ArrayList<AAbilityItem>> = HashMap<String,ArrayList<AAbilityItem>>()
    val VODKA : Vodka = Vodka()
    val AREACORE_ITEM : AreaCoreItem = AreaCoreItem()
    val TIPBLOCK_CONTENT : TipBlockContent = TipBlockContent()
    val MULTI_TOOL : Multitool = Multitool(null)
    val DRAWING:Drawing = Drawing("空图纸")


    fun searchItem(name: String) : AAbilityItem?{
        ItemList.allItem().forEach{
            if (it.name() == name){
                return it
            }
        }
        return null
    }
    fun register(plugin:String,item: AAbilityItem){
        if (!hooks.containsKey(plugin)){
            hooks[plugin] = ArrayList<AAbilityItem>()
        }
        var list = hooks[plugin]
        list!!.add(item)
        hooks[plugin] = list
    }
    fun get(plugin:String,name:String): AAbilityItem {
        return hooks[plugin]!!.filter { name != it.name() }[0]
    }
    fun get(plugin:String) : ArrayList<AAbilityItem> {
        return hooks[plugin]!!
    }
    fun getAll() : ArrayList<AAbilityItem>{
        var list = ArrayList<AAbilityItem>()
        hooks!!.values.forEach{
            it.forEach{
                list.add(it)
            }
        }
        return list
    }
    fun allItem() : ArrayList<AAbilityItem>{
        var ilist = ArrayList<AAbilityItem>()
        ItemList.javaClass.declaredFields.filter { !it.name.equals("INSTANCE") }.filter { !it.name.equals("hooks") }.map { it.isAccessible = true; it.get(null) as AAbilityItem }.forEach {
            ilist.add(it)
        }
        ilist.addAll(ItemList.getAll())
        return ilist
    }
}