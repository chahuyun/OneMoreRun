package cn.chahuyun.omr.plugin

import cn.chahuyun.hibernateplus.DriveType.MYSQL
import cn.chahuyun.hibernateplus.HibernatePlusService
import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.config.DataConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object DataManager {

    private val log = KotlinLogging.logger { }

    @OptIn(ConsoleExperimentalApi::class)
    fun init(plugin: JvmPlugin) {
        val configuration = HibernatePlusService.createConfiguration(plugin::class.java)

        configuration.classLoader = plugin::class.java.classLoader
        configuration.packageName = "cn.chahuyun.omr.entity.data"

        val dataType = DataConfig.type
        configuration.driveType = dataType
        when (dataType) {
            MYSQL -> {
                configuration.address = DataConfig.url
                configuration.user = DataConfig.user
                configuration.password = DataConfig.password
            }

            else -> {
                log.warn { "请配置数据库为MYSQL类型!" }
                MiraiConsole.shutdown()
                return
            }
        }

        HibernatePlusService.loadingService(configuration)
        OneMoreRun.logger.info("OneMoreRun DateBase loaded success fully !")

    }

}