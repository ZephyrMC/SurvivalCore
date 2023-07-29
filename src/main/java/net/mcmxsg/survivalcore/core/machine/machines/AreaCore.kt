package net.mcmxsg.survivalcore.core.machine.machines

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import de.tr7zw.changeme.nbtapi.NBTBlock
import de.tr7zw.changeme.nbtapi.NBTItem
import net.bxx2004.pandalib.bukkit.gui.HolderFactory
import net.bxx2004.pandalib.bukkit.gui.predefine.CommonMenu
import net.bxx2004.pandalib.bukkit.gui.predefine.FlipMenu
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.application.PConversation
import net.bxx2004.pandalib.bukkit.language.abandon.PMessage
import net.bxx2004.pandalib.bukkit.language.abandon.PTitle
import net.bxx2004.pandalib.bukkit.util.PMath
import net.bxx2004.pandalib.bukkit.util.PPlugin
import net.bxx2004.pandalib.bukkit.hook.PVault
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import net.mcmxsg.survivalcore.core.api.event.ItemAccessEvent
import net.mcmxsg.survivalcore.data.Config
import net.mcmxsg.survivalcore.data.ItemList
import net.mcmxsg.survivalcore.util.intofunction.ItemStackFunction.writeMachine
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


//areacore{l:10,w:10,h:10,owner:ZZZiChuan,id:}
class AreaCore : ABlockMachine(),Listener  {
    override fun accessItem(e: ItemAccessEvent) {

    }

    override fun onBlock(block: Block,player: Player) {

    }
    override fun name(): String {
        return "营地核心"
    }

    override fun blockPlace(e: BlockPlaceEvent) : BlockPlaceEvent{
        if (e.block.world.name != "world"){
            PTitle.To(e.player,"&c仅可在开放世界放置")
            return e
        }
        var itemC = NBTItem(e.itemInHand).getCompound("survivalcore").getCompound("machinedata").getCompound("areacore")
        var x1 = e.block.x + itemC.getInteger("l")
        var z1 = e.block.z + itemC.getInteger("w")
        var y1 = e.block.y + itemC.getInteger("y")
        var x2 = e.block.x - itemC.getInteger("l")
        var z2 = e.block.z - itemC.getInteger("w")
        var y2 = e.block.y - itemC.getInteger("y")
        val min: BlockVector3 = BlockVector3.at(x1, y1, z1)
        val max: BlockVector3 = BlockVector3.at(x2, y2, z2)
        val region: ProtectedRegion = ProtectedCuboidRegion(itemC.getString("id"), min, max)
        val container = WorldGuard.getInstance().platform.regionContainer
        val regions = container[BukkitAdapter.adapt(e.block.world)]
        if (regions!!.getApplicableRegions(region).regions.size > 0){
            e.isCancelled = true
            PTitle.To(e.player,"&c此处与其他玩家营地相邻")
        }else{
            var areaC = data(e.block).addCompound("areacore")
            areaC.setInteger("l",itemC.getInteger("l"))
            areaC.setInteger("w",itemC.getInteger("w"))
            areaC.setInteger("h",itemC.getInteger("h"))
            areaC.setString("owner",itemC.getString("owner"))
            areaC.setString("id",itemC.getString("id"))
            areaC.setString("name",itemC.getString("id"))
            data(e.block).mergeCompound(areaC)
            regions!!.addRegion(region)
            regions.save()
        }
        return e
    }

    override fun temple(): PItemStack {
        return PItemStack(Material.GLASS,"&7营地核心"," ","&7在世界内放下该核心创造自己的营地","&7营地内他人无法破坏和交互")
    }

    override fun blockBreak(e: BlockBreakEvent): BlockBreakEvent {
        e.isCancelled = true
        PTitle.To(e.player,"&c无法挖掘&7营地核心,&c请营地管理员手动破碎")
        return e
    }

    override fun interact(e: PlayerInteractEvent) {
        PTitle.To(e.player,"&7${AreaBlockData(e.clickedBlock!!).name()}&nbsp&f" + AreaBlockData(e.clickedBlock!!).owner() + " &7的营地")
    }

    override fun f(e: PlayerUseFEvent) {
        var a = AreaBlockData(e.player.getTargetBlockExact(3)!!)
        if (a.owner() == e.player.name){
            AreaAdminGUI(a).open(e.player)
        }else{
            PTitle.To(e.player,"&c它属于: ${a.owner()},而你是:${e.player.name}")
        }
    }
    class AreaAdminGUI(data:AreaBlockData) : CommonMenu("营地管理: " +data.name(),27){
        var data:AreaBlockData
        init {
            this.data = data
        }

        override fun click(event: InventoryClickEvent) {
            event.isCancelled = true
            event.whoClicked.closeInventory()
            when(event.rawSlot){
                10 -> {
                    var conservation = PConversation("&7请输入修改的名称(输入cancel取消)",false)
                    conservation.start(event.whoClicked as Player)
                    object : BukkitRunnable(){
                        override fun run() {
                            if (conservation.answer() != null){
                                var name = conservation.answer()
                                if (name.length > 10 || name.length < 2){
                                    PMessage.to(event.whoClicked,"&c名字太长或太短")
                                }else if (name.contains("党") || name.contains("国") ||name.contains("习近平") ||
                                    name.contains("政治") ||name.contains("恐") ||name.contains("傻") || name.contains("逼") ||
                                    name.contains("SB") || name.contains("sb") ||name.contains("nmsl") ||name.contains("NMSL")){
                                    PMessage.to(event.whoClicked,"&c不能有敏感词")
                                }else{
                                    val container = WorldGuard.getInstance().platform.regionContainer
                                    val regions = container[BukkitAdapter.adapt(data.block.world)]
                                    regions!!.getApplicableRegions(BlockVector3.at(data.block.x,data.block.y,data.block.z)).regions.forEach(){
                                        if (it.id == data.id()){
                                            data.rename(name)
                                            PMessage.to(event.whoClicked,"&a修改成功,当前名为:&f $name")
                                        }
                                    }
                                }
                                conservation.end(event.whoClicked as Player)
                                cancel()
                            }
                        }
                    }.runTaskTimerAsynchronously(PPlugin.getPlugin("SurvivalCore"),0,20)
                }
                12 -> {
                    val container = WorldGuard.getInstance().platform.regionContainer
                    val regions = container[BukkitAdapter.adapt(data.block.world)]
                    regions!!.getApplicableRegions(BlockVector3.at(data.block.x,data.block.y,data.block.z)).regions.forEach(){
                        AreaPeoPleGUI(it).open(event.whoClicked as Player)
                    }
                }
                14 -> {
                    event.whoClicked.closeInventory()
                    PTitle.To(event.whoClicked as Player,"&c没什么可以设置的")
                }
                16 -> {
                    event.whoClicked.closeInventory()
                    var conservation = PConversation("&7请输入(确认删除)来删除该营地(输入cancel取消)",false)
                    conservation.start(event.whoClicked as Player)
                    object : BukkitRunnable(){
                        override fun run() {
                            if (conservation.answer() != null){
                                var name = conservation.answer()
                                if (name == "确认删除"){
                                    val container = WorldGuard.getInstance().platform.regionContainer
                                    val regions = container[BukkitAdapter.adapt(data.block.world)]
                                    regions!!.removeRegion(data.id())
                                    data.block.type = Material.AIR
                                    data.block.world.dropItem(data.block.location,ItemList.AREACORE_ITEM.asItem())
                                    PMessage.to(event.whoClicked,"&c删除成功")
                                    NBTBlock(data.block).data.removeKey("survivalcore")
                                }
                                conservation.end(event.whoClicked as Player)
                                cancel()
                            }
                        }
                    }.runTaskTimer(PPlugin.getPlugin("SurvivalCore"),0,20)
                }
            }
        }
        override fun layout(): HashMap<Int, PItemStack> {
            val items : HashMap<Int, PItemStack> = HashMap<Int, PItemStack>()
            var b = "${data.l() * 2} * ${data.w() * 2} * ${data.h() * 2}"
            items[10] = PItemStack(Material.GLASS,"&9营地基本信息:"," ","&7营地名称:&f ${data.name()}","&7营地主人:&f ${data.owner()}","&7营地大小:&f $b"," ","&c点击可更改营地名称")

            items[12] = PItemStack(Material.LANTERN,"&9营地成员管理","&7成员将会拥有该营地的交互权")
            items[14] = PItemStack(Material.ANVIL,"&9营地设置")
            items[16] = PItemStack(Material.REDSTONE,"&c营地删除")
            return items
        }
    }
    class AreaPeoPleGUI(region: ProtectedRegion) : FlipMenu("营地成员管理"){
        private var region: ProtectedRegion
        init {
            this.region = region
            val items: MutableList<ItemStack> = ArrayList()
            region.members.uniqueIds.forEach(){
                items.add(PItemStack(Material.ARMOR_STAND,Bukkit.getOfflinePlayer(it).name,"&7成员","&c单击移除该成员"))
            }
            for (i in 0 until PMath.formatNumber(items.size, 35)) {
                val itemStack = arrayOfNulls<PItemStack>(35)
                for (o in 0..34) {
                    try {
                        itemStack[o] = PItemStack(items[o + i * 35])
                    } catch (e: Exception) {
                        break
                    }
                }
                setItem(i, *itemStack)
            }
            inventory().setItem(49,PItemStack(Material.BLUE_DYE,"&7添加成员"))
        }

        override fun click(event: InventoryClickEvent?) {
            event!!.isCancelled = true
            if (event.currentItem!!.type == Material.ARMOR_STAND){
                region.members.removePlayer(Bukkit.getOfflinePlayer(event.currentItem!!.itemMeta.displayName).uniqueId)
                event.whoClicked.closeInventory()
                PMessage.to(event.whoClicked,"&c移除成员 ${event.currentItem!!.itemMeta.displayName} 成功")
            }
            if (event.currentItem!!.type == Material.ARROW){
                inventory().setItem(49,PItemStack(Material.BLUE_DYE,"&7添加成员"))
            }
            if (event.currentItem!!.type == Material.BLUE_DYE){
                event.whoClicked.closeInventory()
                var conservation = PConversation("&7请输入成员的名称(输入cancel取消)",false)
                conservation.start(event.whoClicked as Player)
                object : BukkitRunnable(){
                    override fun run() {
                        if (conservation.answer() != null){
                            var name = conservation.answer()
                            if (Bukkit.getOfflinePlayer(name).isOnline){
                                region.members.addPlayer(Bukkit.getOfflinePlayer(name).uniqueId)
                                PMessage.to(event.whoClicked,"&a成员添加成功")
                            }else{
                                PMessage.to(event.whoClicked,"&c该玩家不在线")
                            }
                            cancel()
                        }
                    }
                }.runTaskTimerAsynchronously(PPlugin.getPlugin("SurvivalCore"),0,20)
            }
            super.click(event)
        }
    }
    class AreaGetGUI() : CommonMenu("营地核心购买",45){
        var l : Int = 30
        var w : Int = 30
        var h : Int = 30
        var owner: String = ""
        var id: String = UUID.randomUUID().toString()
        var money: Int = 0
        override fun holder(): InventoryHolder {
            return HolderFactory.register(this.toString(),true)
        }

        override fun click(event: InventoryClickEvent) {
            var p = event.whoClicked as Player
            event.isCancelled = true
            when (event.slot){
                11 -> {
                    if (l < 100){
                        l++
                        update(p)
                    }
                }
                13 -> {
                    if (w < 100){
                        w++
                        update(p)
                    }
                }
                15 -> {
                    if (h < 100){
                        h++
                        update(p)
                    }
                }
                29 -> {
                    if (l > 10){
                        l--
                        update(p)
                    }
                }
                31 -> {
                    if (w > 10){
                        w--
                        update(p)
                    }
                }
                33 -> {
                    if (h > 10){
                        h--
                        update(p)
                    }
                }
                40 -> {
                    event.whoClicked.closeInventory()
                    if (PVault.checkEconomy(p) >= money){
                        if (p.inventory.firstEmpty() == -1){
                            PMessage.to(p,Config.prefix + "&7您背包空间不足...")
                        }else{
                            PMessage.to(p,Config.prefix + "&7感谢光临...")
                            var result = inventory().getItem(4)!!
                            event.whoClicked.inventory.addItem(AreaItemData(result).write(l,w,h,owner,id).writeMachine("营地核心"))
                            PVault.removeEconomy(p, money.toDouble())
                        }
                    }else{
                        PMessage.to(p,Config.prefix + "&7您的钱不够...")
                    }
                }
            }
        }
        fun update(player: Player){
            var a = PItemStack(Material.GLASS,"&7营地核心"," ","&7在世界内放下该核心创造自己的营地","&7营地内他人无法破坏和交互").writeMachine("营地核心")
            a.addLore(" ","&7当前营地两侧长: &f$l","&7当前营地两侧宽: &f$w","&7当前营地两侧高: &f$h")
            inventory().setItem(4,a)
            var group = PVault.getGroup(player)
            money = if (group.equals("vip")){
                l * h *w * 1
            }else{
                l * h * w * 3
            }
            inventory().setItem(40,PItemStack(Material.CHEST,"&a点击购买","&7目前共计: $money 元"))
        }
        override fun notClickString(): Array<String> {
            return arrayOf<String>(" ")
        }

        override fun notClickSlot(): IntArray {
            return intArrayOf(4,11,13,15,29,31,33,40)
        }

        override fun open(event: InventoryOpenEvent) {
            owner = event.player.name
            var group = PVault.getGroup(event.player as Player)
            update(event.player as Player)
        }
        override fun layout(): HashMap<Int, PItemStack> {
            val items : HashMap<Int, PItemStack> = HashMap<Int, PItemStack>()
            items[11] = PItemStack(Material.GREEN_STAINED_GLASS_PANE,"&a营地两侧长增加 1")
            items[13] = PItemStack(Material.GREEN_STAINED_GLASS_PANE,"&a营地两侧宽增加 1")
            items[15] = PItemStack(Material.GREEN_STAINED_GLASS_PANE,"&a营地两侧高增加 1")
            items[29] = PItemStack(Material.RED_STAINED_GLASS_PANE,"&c营地两侧长减少 1")
            items[31] = PItemStack(Material.RED_STAINED_GLASS_PANE,"&c营地两侧宽减少 1")
            items[33] = PItemStack(Material.RED_STAINED_GLASS_PANE,"&c营地两侧高减少 1")
            return items
        }
    }
    class AreaBlockData(block:Block){
        var block : Block
        var nbt = NBTBlock(block).data.getCompound("survivalcore").getCompound("machinedata").getCompound("areacore")
        init {
            this.block = block
        }
        fun rename(a:String){
            nbt.setString("name",a)
        }
        fun l() : Int{
            return nbt.getInteger("l")
        }
        fun w() : Int{
            return nbt.getInteger("w")
        }
        fun h() : Int{
            return nbt.getInteger("h")
        }
        fun owner() : String{
            return nbt.getString("owner")
        }
        fun id() : String{
            return nbt.getString("id")
        }
        fun name() : String{
            return nbt.getString("name")
        }
    }
    class AreaItemData(item:ItemStack){
        var nbtitem = NBTItem(item)
        var nbt = nbtitem.addCompound("survivalcore").addCompound("machinedata").addCompound("areacore")
        fun l() : Int{
            return nbt.getInteger("l")
        }
        fun w() : Int{
            return nbt.getInteger("w")
        }
        fun h() : Int{
            return nbt.getInteger("h")
        }
        fun owner() : String{
            return nbt.getString("owner")
        }
        fun id() : String{
            return nbt.getString("id")
        }
        fun write(l:Int,w:Int,h:Int,owner:String,id:String) : PItemStack{
            nbt.setInteger("l",l)
            nbt.setInteger("w",w)
            nbt.setInteger("h",h)
            nbt.setString("owner",owner)
            nbt.setString("id",id)
            nbtitem.mergeCompound(nbt)
            return PItemStack(nbtitem.item)
        }
    }
}