package net.mcmxsg.survivalcore.core.util

import org.bukkit.block.Block
import java.util.function.Consumer

class MachineState {
    val DOING = "DOING"
    val STAY = "STAY"
    private var maps: HashMap<Block, String> = HashMap<Block, String>()
    fun set(block: Block,state:String){
        maps[block] = state
    }
    fun default(block: Block){
        maps[block] = STAY
    }
    fun remove(block: Block){
        maps.remove(block)
    }
    fun state(block: Block) : String{
        return maps[block]!!
    }
    fun isDoing(block: Block):Boolean{
        return maps[block] == DOING
    }
    fun isStay(block: Block):Boolean{
        return maps[block] == STAY
    }
    fun isState(block: Block,state: String) : Boolean{
        return maps[block] == state
    }
    fun ifDoElseDo(block: Block,state: String,consumer: Consumer<String>,consumer1: Consumer<String>){
        if (isState(block, state)){
            consumer.accept(state)
        }else{
            consumer1.accept(state)
        }
    }
}