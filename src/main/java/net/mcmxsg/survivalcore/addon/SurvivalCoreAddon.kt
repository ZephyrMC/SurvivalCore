package net.mcmxsg.survivalcore.addon

import net.bxx2004.pandalibloader.Path
import java.io.File
import java.util.function.Consumer

abstract class SurvivalCoreAddon{
    private val packageName: String = this.javaClass.getAnnotation(Path::class.java).pack
    abstract fun start()
    abstract fun stop()
    abstract fun name(): String
    abstract fun version(): String
    abstract fun author() : String
    fun dataPath() : String{
        var file = File("plugins/SurvivalCore/addon/${name()}")
        if (!file.exists()){
            file.mkdirs()
        }
        return file.absolutePath
    }
    open fun scan(consumer: Consumer<String>){
        consumer.accept(packageName)
    }
}