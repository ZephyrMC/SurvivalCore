package net.mcmxsg.survivalcore.data

import net.bxx2004.pandalib.bukkit.file.data.config.Config
import net.bxx2004.pandalib.bukkit.file.data.config.Node

@Config(path = "plugins/SurvivalCore/config.yml")
object Config {
    @Node(key = "preifx")
    var prefix: String = "&c[末世求生录]"
    @Node(key = "accessItemDistance")
    var accessItemDistance: Int = 1
    @Node(key = "mysql.ip")
    var mysqlIP: String = "localhost:3306"
    @Node(key = "mysql.dbname")
    var dbName: String = "survivalcore"
    @Node(key = "mysql.username")
    var mysqlUserName: String = "bxx2004"
    @Node(key = "mysql.password")
    var mysqlPassword: String = "bxx2004"
    @Node(key = "mysql.tablename")
    var mysqlTableName: String = "areas"
}