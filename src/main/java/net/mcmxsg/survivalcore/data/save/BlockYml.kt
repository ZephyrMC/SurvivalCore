package net.mcmxsg.survivalcore.data.save

import net.bxx2004.pandalib.bukkit.file.PYml
import net.bxx2004.pandalib.bukkit.gui.predefine.CommonMenu
import net.bxx2004.pandalib.bukkit.gui.predefine.StructuralMenu
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PMessage
import net.bxx2004.pandalib.bukkit.language.component.Title
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.function.Consumer

class BlockYml(block: Block) : PYml("plugins/SurvivalCore/blockdata/${block.location}.yml",true){
    var block :Block
    init {
        this.block = block
    }
    companion object{
        @JvmStatic
        var cache = HashMap<Block,BlockMenu>()
    }
    fun setInventory(title: String,inventory: Inventory){
        var i = 0
        set("inventory.title",title)
        set("inventory.size",inventory.size)
        inventory.contents!!.forEach {
            if (it == null){
                set("inventory.items.$i",ItemStack(Material.AIR))
            }else{
                set("inventory.items.$i",it)
            }
            i++
        }
        cache[block] = getInventory()
    }
    fun hasInventory() : Boolean{
        return keys().contains("inventory")
    }
    fun getInventoryOrDefault(title: String,inventory: Inventory) : BlockMenu{
        if (!hasInventory()){
            setInventory(title,inventory)
        }
        return getInventory()
    }
    fun getInventory() : BlockMenu{
        if (cache.containsKey(block)){
            return cache[block]!!
        }
        cache[block] = BlockMenu(this,getString("inventory.title"),getInt("inventory.size"))
        return cache[block]!!
    }
    fun clearInventory(){
        set("inventory",null)
    }
    fun clearFile(){
        var file = File("plugins/SurvivalCore/blockdata/${block.location}.yml")
        file.delete()
    }
    class BlockMenu(blockYml: BlockYml,title: String,size:Int) : CommonMenu(title, size){
        private var blockYml:BlockYml
        private var title:String
        private var size : Int
        private var inventory:Inventory
        private lateinit var openConsumer : Consumer<InventoryOpenEvent>
        private lateinit var clickConsumer : Consumer<InventoryClickEvent>
        init {
            this.title = title
            this.size = size
            this.blockYml = blockYml
            this.inventory = Bukkit.createInventory(holder(),size,title)
        }
        override fun close(event: InventoryCloseEvent) {
            blockYml.setInventory(title,inventory())
        }
        fun onOpen(consumer: Consumer<InventoryOpenEvent>): BlockMenu{
            this.openConsumer = consumer
            return this
        }

        override fun inventory(): Inventory {
            return inventory
        }

        override fun setItem(i: Int, stack: PItemStack) {
            inventory.setItem(i,stack)
            blockYml.set("inventory.items.$i",ItemStack(stack))
        }
        fun onClick(consumer: Consumer<InventoryClickEvent>) : BlockMenu{
            this.clickConsumer = consumer
            return this
        }

        override fun open(event: InventoryOpenEvent?) {
            blockYml.keys("inventory.items").forEach {
                inventory().setItem(it.toInt(),blockYml.getItem("inventory.items.$it"))
            }
            openConsumer.accept(event!!)
        }

        override fun click(event: InventoryClickEvent?) {
            if (event!!.currentItem!!.itemMeta.displayName == PMessage.replace("&c禁止点击")){
                event.isCancelled = true
            }
            clickConsumer.accept(event!!)
        }
    }
}