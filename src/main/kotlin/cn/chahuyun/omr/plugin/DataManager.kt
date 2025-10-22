package cn.chahuyun.omr.plugin

import cn.chahuyun.hibernateplus.DriveType.*
import cn.chahuyun.hibernateplus.HibernatePlusService
import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.config.DataConfig
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin

object DataManager {
    fun init(plugin: JavaPlugin) {
        val configuration = HibernatePlusService.createConfiguration(plugin::class.java)

        configuration.classLoader = plugin::class.java.classLoader
        configuration.packageName = "cn.chahuyun.omr.entity"

        val dataType = DataConfig.type
        configuration.driveType = dataType
        when (dataType) {
            MYSQL -> {
                configuration.address = DataConfig.url
                configuration.user = DataConfig.user
                configuration.password = DataConfig.password
            }

            H2 -> configuration.address = OneMoreRun.dataFolderPath.resolve("authorize.h2.mv.db").toString()
            SQLITE -> configuration.address = OneMoreRun.dataFolderPath.resolve("authorize.mv.db").toString()
        }

        HibernatePlusService.loadingService(configuration)
        OneMoreRun.logger.info("OneMoreRun DateBase loaded success fully !")

    }

}