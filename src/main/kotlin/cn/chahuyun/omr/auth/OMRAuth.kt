package cn.chahuyun.omr.auth

import cn.chahuyun.authorize.PermissionServer
import cn.chahuyun.authorize.entity.Perm
import cn.chahuyun.authorize.utils.PermUtil
import cn.chahuyun.hibernateplus.HibernateFactory
import cn.chahuyun.omr.OneMoreRun

object OMRAuth {

    const val PERM_OMR_BASE = "omr-base"
    const val PERM_OMR_BASE_ST = "再来亿把插件-基本权限"
    const val PERM_OMR_BASE_G = "再来亿把插件-基本权限组"


    const val PERM_OMR_USER = "omr-user"
    const val PERM_OMR_USER_ST = "再来亿把插件-用户权限"
    const val PERM_OMR_USER_G = "再来亿把插件-用户权限组"

    const val PERM_OMR_GAME = "omr-game"
    const val PERM_OMR_GAME_ST = "再来亿把插件-游戏权限"
    const val PERM_OMR_GAME_G = "再来亿把插件-游戏权限组"


    /**
     * 权限加载器函数，用于注册和配置OneMoreRun相关的权限及权限组
     *
     * 该函数主要完成以下功能：
     * 1. 注册OneMoreRun相关的权限码
     * 2. 创建或更新基础权限组、用户权限组和游戏权限组
     * 3. 建立权限组之间的父子关系
     */
    fun loader() {
        // 注册OneMoreRun权限码，包括基础权限、用户权限和游戏权限
        PermissionServer.registerPermCode(
            OneMoreRun,
            Perm(PERM_OMR_BASE, PERM_OMR_BASE_ST),
            Perm(PERM_OMR_USER, PERM_OMR_USER_ST),
            Perm(PERM_OMR_GAME, PERM_OMR_GAME_ST)
        )

        // 配置基础权限组，确保基础权限已添加到组中
        val baseGroup = PermUtil.talkPermGroupByName(PERM_OMR_BASE_G)
        val basePerm = PermUtil.takePerm(PERM_OMR_BASE)
        if (!baseGroup.perms.contains(basePerm)) {
            PermUtil.addPermToPermGroupByPermGroup(basePerm, baseGroup)
        }

        // 配置用户权限组，确保用户权限已添加到组中，并设置父级为基础权限组
        val userGroup = PermUtil.talkPermGroupByName(PERM_OMR_USER_G)
        val userPerm = PermUtil.takePerm(PERM_OMR_USER)
        if (!userGroup.perms.contains(userPerm)) {
            PermUtil.addPermToPermGroupByPermGroup(userPerm, userGroup)
        }
//        if (userGroup.parentId == null) {
//            userGroup.parentId = baseGroup.id
//        }
//        userGroup.save()

        // 配置游戏权限组，确保游戏权限已添加到组中，并设置父级为基础权限组
        val gameGroup = PermUtil.talkPermGroupByName(PERM_OMR_GAME_G)
        val gamePerm = PermUtil.takePerm(PERM_OMR_GAME)
        if (!gameGroup.perms.contains(gamePerm)) {
            PermUtil.addPermToPermGroupByPermGroup(gamePerm, gameGroup)
        }
//        if (gameGroup.parentId == null) {
//            gameGroup.parentId = baseGroup.id
//        }
//        gameGroup.save()
    }


}

