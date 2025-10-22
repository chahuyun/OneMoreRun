package cn.chahuyun.omr

import cn.chahuyun.omr.config.DataConfig
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.MiraiLogger

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
    }


    override fun onDisable() {

    }

}