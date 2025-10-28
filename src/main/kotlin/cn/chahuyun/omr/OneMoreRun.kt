package cn.chahuyun.omr

import cn.chahuyun.omr.config.DataConfig
import cn.chahuyun.omr.plugin.DataManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object OneMoreRun : KotlinPlugin(
    JvmPluginDescription(
        "cn.chahuyun.one-more-run", "1.0.0",
        "再刷亿把"
    ) {
        author("moyuyanli & firefairy198")
    }
) {

    override fun onEnable() {
        DataConfig.reload()
        DataManager.init(this)
    }


    override fun onDisable() {

    }

}