package net.mcmxsg.survivalcore.addon

import net.bxx2004.pandalib.bukkit.manager.Lang
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile


class AddonJarLoader{
    companion object{
        @JvmStatic
        fun main(url:URL,b:Boolean,classLoader: ClassLoader) {
            var urlClassLoader: URLClassLoader? = null
            var mainClass: Class<*>? = null
            try {
                urlClassLoader = URLClassLoader(arrayOf(url),classLoader)
                var jarFile = JarFile(url.file)
                var allFile = jarFile.entries()
                while (allFile.hasMoreElements()){
                    var en = allFile.nextElement()
                    if (en.name.endsWith("class")){
                        var clazz = urlClassLoader.loadClass(en.name.replace(".class","").replace("/","."))
                        if (clazz.superclass != null && clazz.superclass.name.contains("SurvivalCoreAddon")){
                            mainClass = clazz
                            break
                        }
                    }
                }
                var obj = mainClass!!.getDeclaredConstructor().newInstance() as SurvivalCoreAddon
                if (b){
                    Lang.print("已加载附属: &c名称: &f${obj.name()} &7| &c作者: &f${obj.author()} &7| &c版本: &f${obj.version()}")
                    obj.start()
                }else{
                    obj.stop()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}