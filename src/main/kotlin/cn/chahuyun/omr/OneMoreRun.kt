package cn.chahuyun.omr

import cn.chahuyun.authorize.PermissionServer
import cn.chahuyun.omr.auth.OMRAuth
import cn.chahuyun.omr.config.DataConfig
import cn.chahuyun.omr.plugin.DataManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object OneMoreRun : KotlinPlugin(
    JvmPluginDescription(
        "cn.chahuyun.one-more-run", "1.0.0",
        "再刷亿把"
    ) {
        dependsOn("cn.chahuyun.HuYanAuthorize", ">=1.2.6", false)
        author("moyuyanli & firefairy198")
    }
) {

    override fun onEnable() {
        DataConfig.reload()
        DataManager.init(this)

        OMRAuth.loader()
        PermissionServer.registerMessageEvent(this, "cn.chahuyun.omr.event")
    }


    override fun onDisable() {

    }

}