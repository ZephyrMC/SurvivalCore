package net.mcmxsg.survivalcore.core.api.event

import net.bxx2004.pandalib.bukkit.listener.event.PandaLibExtendEvent
import net.mcmxsg.survivalcore.core.api.core.ABlockMachine
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Item

class ItemAccessEvent(machine: ABlockMachine, item: Item, source:Entity, block: Block) : PandaLibExtendEvent() {
    var item: Item
    var machine : ABlockMachine
    var source: Entity
    var block : Block
    init {
        this.block = block
        this.source = source
        this.item = item
        this.machine = machine
    }
}