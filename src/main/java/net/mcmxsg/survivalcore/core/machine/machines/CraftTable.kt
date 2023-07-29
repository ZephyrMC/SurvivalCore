package net.mcmxsg.survivalcore.core.machine.machines

import net.bxx2004.pandalib.PandaLib
import net.bxx2004.pandalib.bukkit.gui.predefine.FlipMenu
import net.bxx2004.pandalib.bukkit.gui.predefine.StructuralMenu
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PActionBar
import net.bxx2004.pandalib.bukkit.rpg.view.Hologram
import net.bxx2004.pandalib.bukkit.util.PMath
import net.mcmxsg.survivalcore.core.api.core.*
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.data.ItemList
import net.mcmxsg.survivalcore.data.MachineList
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.clearMachine
import net.mcmxsg.survivalcore.util.intofunction.BlockFunction.writeLock
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.getAbility
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.getMachine
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.hasAbility
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.hasMachine
import net.mcmxsg.survivalcore.util.intofunction.PlayerFunction.targetBlock
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.Chest
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable

class CraftTable : ABlockMachine() {
    companion object{
        val craftMap : HashMap<String,BlockItem> = HashMap<String,BlockItem>()
        val craftTask : HashMap<String,BukkitRunnable> = HashMap<String,BukkitRunnable>()
        val hologMap : HashMap<String,Hologram> = HashMap()
    }
    override fun name(): String {
        return "物品合成台"
    }

    override fun temple(): PItemStack {
        return PItemStack(Material.CRAFTING_TABLE,"&7物品合成台","&cF&7 选择需要合成的物品","&c右键 &7合成物品","&7依据展示按顺序放入合成材料")
    }

    override fun f(e: PlayerUseFEvent) {
        if (craftTask.containsKey(e.player.targetBlock().location.toString())) {
            PActionBar.To(e.player, "&c正在制作中")
        }else{
            Table(e.player.targetBlock()).open(e.player)
        }
    }

    override fun blockPlace(e: BlockPlaceEvent): BlockPlaceEvent {
        e.block.getRelative(BlockFace.UP).type = Material.CHEST
        e.block.getRelative(BlockFace.UP).writeLock(false)
        hologMap[e.block.location.toString()] = Hologram(e.block.toString())
            .distance(0.4)
            .content("&9[物品合成台]","&c当前制作物品:&f无 &7| &c剩余时间:&f无").build(e.block.location.clone().add(0.50,2.50,0.5))
        return e
    }

    override fun blockBreak(e: BlockBreakEvent): BlockBreakEvent {
        e.block.getRelative(BlockFace.UP).type = Material.AIR
        e.block.getRelative(BlockFace.UP).clearMachine()
        if (craftMap.containsKey(e.block.location.toString())) {
            craftMap.remove(e.block.location.toString())
        }
        if (craftTask.containsKey(e.block.location.toString())) {
            craftTask[e.block.location.toString()]!!.cancel()
            craftTask.remove(e.block.location.toString())
        }
        if (hologMap.containsKey(e.block.location.toString())){
            hologMap[e.block.location.toString()]!!.remove()
            hologMap.remove(e.block.location.toString())
        }
        return e
    }
    override fun interact(e: PlayerInteractEvent) {
        if (e.action == Action.RIGHT_CLICK_BLOCK){
            e.isCancelled = true
            var block : Block = e.clickedBlock!!
            if (!hologMap.containsKey(block.location.toString())){
                block.location.clone().add(0.5,1.0,0.5).getNearbyEntitiesByType(ArmorStand::class.java,1.0,3.0).forEach{
                    it.remove()
                }
                return
            }
            if (chest(block) == null){
                PActionBar.To(e.player,"&c没箱子做不了")
            }else{
                if (craftTask.containsKey(e.clickedBlock!!.location.toString())){
                    PActionBar.To(e.player,"&c正在制作中")
                }else{
                    if (craftMap.containsKey(e.clickedBlock!!.location.toString())){
                        var make = craftMap[e.clickedBlock!!.location.toString()] as Craftable<BlockItem>
                        if (make.require(e.player)){
                            if (arrayEqual(make.material(),chest(e.clickedBlock!!)!!)){
                                PActionBar.To(e.player,"&a开始制作")
                                craftTask[e.clickedBlock!!.location.toString()] = object : BukkitRunnable(){
                                    var i = 0
                                    override fun run() {
                                        if (i == make.time()){
                                            if (isResult(e.clickedBlock!!,make,e.player,chest(e.clickedBlock!!)!!)){
                                                hologMap[e.clickedBlock!!.location.toString()]!!.replace(1,"&c当前制作物品:&f${craftMap[e.clickedBlock!!.location.toString()]!!.name()} &7| &a制作成功")
                                            }else{
                                                hologMap[e.clickedBlock!!.location.toString()]!!.replace(1,"&c当前制作物品:&f${craftMap[e.clickedBlock!!.location.toString()]!!.name()} &7| &c制作失败")
                                            }
                                            craftTask.remove(e.clickedBlock!!.location.toString())
                                            cancel()
                                        }else{
                                            hologMap[e.clickedBlock!!.location.toString()]!!.replace(1,"&c当前制作物品:&f${craftMap[e.clickedBlock!!.location.toString()]!!.name()} &7| &c剩余时间:&f${make.time() - i}")
                                            i++
                                        }
                                    }
                                }
                                craftTask[e.clickedBlock!!.location.toString()]!!.runTaskTimer(PandaLib.initPlugin,0,20)
                                PActionBar.To(e.player,"&c请不要拿走东西哦，否则会失败")
                            }else{
                                PActionBar.To(e.player,"&c材料不足")
                            }
                        }else{
                            PActionBar.To(e.player,"&c未满足合成要求")
                        }
                    }else{
                        PActionBar.To(e.player,"&c请选择要做的东西")
                    }
                }
            }
        }
    }
    fun isResult(block: Block,item : Craftable<BlockItem>,player: Player,chest:ArrayList<PItemStack>): Boolean{
        if (item.chance(player,item as BlockItem) && arrayEqual(item.material(),chest)){
            if (block.getRelative(BlockFace.UP).state is Chest){
                val data = block.getRelative(BlockFace.UP).state as Chest
                data.blockInventory.clear()
                data.blockInventory.addItem(item.asItem())
            }
            return true
        }
        return false
    }
    fun arrayEqual(items : ArrayList<PItemStack>,items1: ArrayList<PItemStack>): Boolean{
        if (items.size != items1.size){
            return false
        }
        var i = 0
        items.forEach {
            if (it.equals(items1[i])){
                i++
            }else{
                return false
            }
        }
        return true
    }
    fun chest(block: Block): ArrayList<PItemStack>?{
        var stacks = ArrayList<PItemStack>()
        if (block.getRelative(BlockFace.UP).state is Chest){
            val data = block.getRelative(BlockFace.UP).state as Chest
            if (data.blockInventory.contents != null){
                data.blockInventory.contents!!.forEach {
                    if (it != null && it.type != Material.AIR){
                        stacks.add(PItemStack(it))
                    }
                }
            }
            return stacks
        }
        return null
    }

    class Table(block: Block) : StructuralMenu("物品合成台",
        "*********",
        "****a****",
        "*********",
        "111111111",
        "111111111",
        "111111111"
    ){
        var block : Block
        init {
            this.block = block
        }
        override fun open(event: InventoryOpenEvent?) {
            setItem('*', PItemStack(Material.BLACK_STAINED_GLASS_PANE))
            if (!craftMap.containsKey(block.location.toString())){
                setItem('a', PItemStack(Material.BARRIER,"&c点击选择需要锻造的物品"))
            }else{
                setItem('a', craftMap[block.location.toString()]!!.asItem())
                var blockItem = craftMap[block.location.toString()]
                if (blockItem is Craftable<*>){
                    var b = blockItem as Craftable<*>
                    for (i in 27 until 53){
                        try {
                            inventory().setItem(i,b.material()[i-27])
                        }catch (e: Exception){}
                    }
                }
            }
        }

        override fun click(event: InventoryClickEvent) {
            event.isCancelled = true
            when(event.rawSlot){
                13 -> {
                    AllItem(block).open(event.whoClicked as Player)
                }
            }
        }
    }



    class AllItem(block: Block) : FlipMenu("所有可合成项目"){
        var block: Block
        init {
            this.block = block
        }
        init {
            var list = ArrayList<BlockItem>()
            ItemList.allItem().forEach {
                if (it is Craftable<*>){
                    list.add(it)
                }
            }
            MachineList.allMachine().forEach {
                if (it is Craftable<*>){
                    list.add(it)
                }
            }
            for (i in 0 until PMath.formatNumber(list.size, 35)) {
                val itemStack = arrayOfNulls<PItemStack>(35)
                for (o in 0..34) {
                    try {
                        itemStack[o] = PItemStack(list[o + i * 35].asItem())
                    } catch (e: Exception) {
                        break
                    }
                }
                setItem(i, *itemStack)
            }
        }

        override fun click(e: InventoryClickEvent) {
            e.isCancelled = true
            if (e.currentItem != null && e.currentItem!!.type != Material.AIR && e.currentItem!!.type != Material.ARROW){
                e.whoClicked.closeInventory()
                if (e.currentItem!!.hasMachine()){
                    craftMap[block.location.toString()] = MachineList.searchMachine(e.currentItem!!.getMachine()!!)!!
                }
                if (e.currentItem!!.hasAbility()){
                    craftMap[block.location.toString()] = ItemList.searchItem(e.currentItem!!.getAbility()!!)!!
                }
                Table(block).open(e.whoClicked as Player)
            }
        }
    }
}