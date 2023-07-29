package net.mcmxsg.survivalcore.core.util

import net.bxx2004.pandalib.bukkit.rpg.view.Hologram
import org.bukkit.block.Block
import org.bukkit.entity.ArmorStand

class MachineHologram {
    private var maps: HashMap<Block,Hologram> = HashMap<Block,Hologram>()
    private fun get(block: Block) : Hologram?{
        if (!has(block)) return null
        return maps[block]
    }
    private fun put(block: Block,hologram: Hologram){
        var mod = hologram::class.java.getDeclaredField("id")
        mod.isAccessible = true
        mod.set(hologram,block.toString())
        if (has(block)) {
            remove(block)
        }
        maps[block] = hologram
    }
    private fun remove(block: Block){
        if (!maps.containsKey(block)){
            block.location.clone().add(0.5,1.0,0.5).getNearbyEntitiesByType(ArmorStand::class.java,1.0,3.0).forEach{
                if (it.scoreboardTags.contains(block.toString())){
                    it.remove()
                }
            }
        }else{
            maps[block]!!.remove()
            maps.remove(block)
        }
    }
    private fun has(block: Block): Boolean{
        return maps.containsKey(block)
    }
    fun initOnPlace(block: Block,hologram: Hologram){
        put(block,hologram)
    }
    fun initOnBreak(block: Block){
        remove(block)
    }
    fun replace(block: Block,int: Int,string: String){
        get(block)!!.replace(int,string)
    }
    fun checkOnInteract(block: Block,hologram: Hologram){
        if (!maps.containsKey(block)){
            block.location.clone().add(0.5,1.0,0.5).getNearbyEntitiesByType(ArmorStand::class.java,1.0,3.0).forEach{
                if (it.scoreboardTags.contains(block.toString())){
                    it.remove()
                }
            }
            put(block,hologram)
        }
    }
}