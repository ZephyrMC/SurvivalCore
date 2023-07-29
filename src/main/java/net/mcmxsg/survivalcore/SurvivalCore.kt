package net.mcmxsg.survivalcore

import net.bxx2004.pandalib.bukkit.hook.PVault
import net.bxx2004.pandalibloader.BukkitPlugin
import net.bxx2004.pandalibloader.KotlinPlugin
import net.bxx2004.pandalibloader.Path
import net.mcmxsg.survivalcore.addon.AddonJarLoader
import net.mcmxsg.survivalcore.data.ItemList
import net.mcmxsg.survivalcore.data.MachineList
import java.io.File
import java.net.URL
import java.util.jar.JarFile

@Path(pack = "net.mcmxsg.survivalcore")
class SurvivalCore : BukkitPlugin() {
    override fun start() {
        ItemList
        MachineList
        startMessage(true)
        PVault()
        var pls = File("plugins/SurvivalCore/addon")
        if (!pls.exists()){
            pls.mkdirs()
        }
        pls.listFiles().forEach {
            AddonJarLoader.main(it.toURL(), true, classLoader)
        }
    }

    override fun load() {

    }

    override fun end() {
        var pls = File("plugins/SurvivalCore/addon")
        pls.listFiles().forEach{
            AddonJarLoader.main(it.toURL(),false,classLoader)
        }
    }
}