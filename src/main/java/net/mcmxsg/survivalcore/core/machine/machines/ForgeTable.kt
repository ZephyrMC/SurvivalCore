package net.mcmxsg.survivalcore.core.machine.machines

import net.bxx2004.pandalib.bukkit.gui.predefine.StructuralMenu
import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PActionBar
import net.mcmxsg.survivalcore.core.api.core.*
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.data.ItemList
import net.mcmxsg.survivalcore.util.intofunction.PlayerFunction.targetBlock
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import java.util.function.Function

class ForgeTable : ABlockMachine(),StorageBlock{
    private var default : StructuralMenu = StructuralMenu("项目锻造台","****a****")
    private var map = HashMap<String,Int>()
    init {
        default.setItem('*', PItemStack(Material.BLACK_STAINED_GLASS_PANE,"&c禁止点击"))
    }
    fun search(name:PItemStack): Forgeable? {
        var list = ArrayList<Forgeable>()
        ItemList.allItem().forEach {
            if (it is Forgeable){
                list.add(it)
            }
        }
        list.forEach { if (it.drawing().asItem().equals(name)) return it }
        return null
    }
    override fun name(): String {
        return "项目锻造台"
    }

    override fun blockBreak(e: BlockBreakEvent): BlockBreakEvent {
        if (map.containsKey(e.block.location.toString())){
            map.remove(e.block.location.toString())
        }
        return e
    }
    override fun temple(): PItemStack {
        return PItemStack(Material.SMITHING_TABLE,"&7项目锻造台")
    }

    override fun interact(e: PlayerInteractEvent) {
        if (e.action == Action.RIGHT_CLICK_BLOCK){
            e.isCancelled = true
            var key = e.clickedBlock!!.location.toString()
            var item = StorageBlock.yml(e.clickedBlock!!).getItem("inventory.items.13")
            if (map.containsKey(key)){
                if (item == null || item.type == Material.AIR){
                    PActionBar.To(e.player,"&c不要拿走图纸")
                    map.remove(key)
                    return
                }
                if (map[key] == 0){
                    map.remove(key)
                    StorageBlock.yml(e.clickedBlock!!).clearInventory()
                    if (search(PItemStack(item))!!.chance(e.player)){
                        PActionBar.To(e.player,"&a锻造成功,物品已经存到锻造台里了")
                        StorageBlock.menu(e.player.targetBlock(),
                            StorageBlock.StructMenuBuilder()
                                .title(name())
                                .type(arrayOf(
                                    "*********",
                                    "****a****",
                                    "*********"
                                ))
                                .icon(Function {
                                    when(it){
                                        '*' -> {
                                            return@Function PItemStack(Material.BLACK_STAINED_GLASS_PANE,"&c禁止点击")
                                        }
                                        'a' -> {
                                            var a = search(PItemStack(item))!! as BlockItem
                                            return@Function a.asItem()
                                        }
                                        else -> {return@Function PItemStack(Material.AIR)}
                                    }
                                })
                                .build()
                        )
                    }else{
                        PActionBar.To(e.player,"&c锻造失败,下次努力")
                    }
                }else{
                    e.player.playSound(e.player.location,Sound.BLOCK_ANVIL_PLACE,5.0F,5.0F)
                    ParticleBuilder(ParticleEffect.FLAME).setAmount(5).setLocation(e.clickedBlock!!.location.clone().add(0.5,1.5,0.5)).display()
                    map[key] = map[key]!! - 1
                    PActionBar.To(e.player,"&c继续努力,还有 &f${map[key]} &c次锻造成功")
                }
            }else{
                if (item != null && item.type != Material.AIR &&search(PItemStack(item)) != null){
                    if (search(PItemStack(item))!!.require(e.player)){
                        map[key] = search(PItemStack(item))!!.forgeNumber()
                    }
                }else{
                    PActionBar.To(e.player,"&c请放入图纸")
                }
            }
        }
    }
    override fun f(e: PlayerUseFEvent) {
        StorageBlock.menu(e.player.targetBlock(),
            StorageBlock.StructMenuBuilder()
                .title(name())
                .type(arrayOf(
                    "*********",
                    "****a****",
                    "*********"
                ))
                .icon(Function {
                    when(it){
                        '*' -> {
                            return@Function PItemStack(Material.BLACK_STAINED_GLASS_PANE,"&c禁止点击")
                        }
                        else -> {return@Function PItemStack(Material.AIR)}
                    }
                })
                .build()
            ).open(e.player)
    }
}