package net.mcmxsg.survivalcore.core.alilityitem.abilityitems

import net.bxx2004.pandalib.bukkit.item.PItemStack
import net.bxx2004.pandalib.bukkit.language.abandon.PMessage
import net.bxx2004.pandalib.bukkit.language.component.ActionBar
import net.bxx2004.pandalib.bukkit.util.PMath
import net.mcmxsg.survivalcore.core.api.core.AAbilityItem
import net.mcmxsg.survivalcore.core.api.core.Forgeable
import net.mcmxsg.survivalcore.core.api.core.Upgradable
import net.mcmxsg.survivalcore.core.api.event.PlayerUseFEvent
import net.mcmxsg.survivalcore.data.ItemList

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

//木 0 铁 1 金 2 钻 3 下 4
class Multitool(nowItem: PItemStack?) : AAbilityItem(),Upgradable<AAbilityItem>,Forgeable{
    private var tagMaterial: Array<Material> = arrayOf(Material.WOODEN_AXE,Material.IRON_AXE,Material.GOLDEN_AXE,Material.DIAMOND_AXE,Material.NETHERITE_AXE)
    private var materialEnd : Array<String> = arrayOf("_AXE","_PICKAXE","_HOE","_SHOVEL")
    var item = PItemStack(Material.WOODEN_AXE,"&7多功能工具 " + Upgradable.UpgradableConstValue.CAN_LEVELTAG," ",Upgradable.UpgradableConstValue.LEVEL(levelTag()[0]),"&cF键转换工具")
    override fun temple(): PItemStack {
        return Upgradable.defaultItem(item,this)
    }

    override fun name(): String {
        return "多功能工具"
    }

    override fun f(e: PlayerUseFEvent) {
        var item = e.player.inventory.itemInMainHand
        item.type = Material.valueOf(item.type.name.split("_")[0] + PMath.getRandomAsString(*materialEnd))
        e.player.inventory.setItemInMainHand(item)
        ActionBar("转换成功").send(e.player)
    }
    override fun up(nowItem: PItemStack): PItemStack {
        var nextLevel = level(nowItem) + 1
        if (nextLevel >= max()) {
            return next(nowItem!!,4)
        }else{
            return next(nowItem!!,nextLevel)
        }
    }
    fun next(item:PItemStack,nextlevel: Int):PItemStack{
        var meta = item.itemMeta
        if (nextlevel >= 4){
            meta.setDisplayName(PMessage.replace("&7多功能工具 " + Upgradable.UpgradableConstValue.MAX_TAG))
        }
        var lore = meta.lore!!
        lore.clear()
        lore.add(" ")
        lore.add(PMessage.replace(Upgradable.UpgradableConstValue.LEVEL(levelTag()[nextlevel])))
        lore.add(PMessage.replace("&cF键转换工具"))
        meta.lore = lore
        item.type = tagMaterial[nextlevel]
        item.itemMeta = meta
        return Upgradable.writeItemLevel(item,nextlevel)
    }
    override fun canUp(nowItem: PItemStack): Boolean {
        return level(nowItem) < max()
    }

    override fun max(): Int {
        return 4
    }

    override fun level(nowItem: PItemStack): Int {
        var max : Int = 0
        if (nowItem != null){
            max = Upgradable.readItemLevel(nowItem!!)
        }
        return max
    }

    override fun material(nextlevel: Int): Array<ItemStack> {
        when(nextlevel){
            1 -> {
                return arrayOf(PItemStack(Material.IRON_INGOT,64))
            }
            2 -> {
                return arrayOf(PItemStack(Material.GOLD_INGOT,64))
            }
            3 -> {
                return arrayOf(PItemStack(Material.DIAMOND,64))
            }
            4 -> {
                return arrayOf(PItemStack(Material.NETHERITE_INGOT,64))
            }
            else -> {
                return arrayOf(PItemStack(Material.BARRIER,1,"&c无法升级"))
            }
        }
    }
    override fun levelTag(): Array<String> {
        return arrayOf("木质","铁质","金质","钻质","合金质")
    }

    override fun type(): Upgradable.Type {
        return Upgradable.Type.ABILITY
    }

    override fun drawing(): Drawing {
        return Drawing("多功能工具")
    }

    override fun forgeNumber(): Int {
        return 999
    }

    override fun chance(player: Player): Boolean {
        return true
    }

    override fun require(player: Player): Boolean {
        return if (player.level >= 50){
            true
        }else{
            player.sendMessage("等级不足")
            false
        }
    }

}