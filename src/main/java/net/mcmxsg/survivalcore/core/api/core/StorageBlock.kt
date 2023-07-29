package net.mcmxsg.survivalcore.core.api.core

import net.bxx2004.pandalib.bukkit.gui.predefine.StructuralMenu
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.mcmxsg.survivalcore.data.save.BlockYml
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.getMachine
import org.bukkit.block.Block
import org.bukkit.inventory.Inventory
import java.util.function.Function

interface StorageBlock {
    companion object{
        fun yml(block: Block) : BlockYml{
            return BlockYml(block)
        }
        fun menu(block: Block, default: StructuralMenu): BlockYml.BlockMenu {
            var block = block
            var data = BlockYml(block)
            return data.getInventoryOrDefault(block.getMachine()!!, default.inventory())
        }
        fun menu(block: Block): BlockYml.BlockMenu {
            var block = block
            var data = BlockYml(block)
            return data.getInventory()
        }
    }
    fun onRemove(yml:BlockYml){
        yml.clearFile()
    }
    fun onPlace(yml:BlockYml){

    }
    class StructMenuBuilder{
        private lateinit var menu: StructuralMenu
        private lateinit var title:String
        private lateinit var index:Array<String>
        var allchar:ArrayList<Char> = ArrayList<Char>()
        fun title(string: String) : StructMenuBuilder{
            title = string
            return this
        }
        fun type(string: Array<String>) : StructMenuBuilder{
            index = string
            index.forEach { it ->
                it.toCharArray().forEach {
                    if (!allchar.contains(it)){
                        allchar.add(it)
                    }
                }
            }
            menu = StructuralMenu(title, *index)
            return this
        }
        fun icon(function: Function<Char,PItemStack>): StructMenuBuilder{
            allchar.forEach {
                menu.setItem(it,function.apply(it))
            }
            return this
        }
        fun build():StructuralMenu{
            return menu
        }
        fun builder(): Inventory{
            return menu.inventory()
        }
    }
}