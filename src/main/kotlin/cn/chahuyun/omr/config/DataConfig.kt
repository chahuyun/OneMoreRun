package cn.chahuyun.omr.config

import cn.chahuyun.hibernateplus.DriveType
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object DataConfig : AutoSavePluginConfig("data-config") {

    @ValueDescription("数据库驱动 H2/SQLITE/MYSQL")
    var type: DriveType by value(DriveType.SQLITE)

    @ValueDescription("数据库链接地址")
    var url: String by value("localhost:3306/one_more_run")

    @ValueDescription("数据库用户名")
    var user: String by value("root")

    @ValueDescription("数据库密码")
    var password: String by value("123456")


}