package cn.chahuyun.omr.auth

/**
 * 权限常量对象，用于定义OMR系统相关的权限标识
 * 该对象包含了OMR模块的基础权限和各类具体权限配置
 */
object OMRPerm {
    /**
     * OMR模块基础权限标识
     * 作为OMR权限体系的根权限，通常用于权限继承和基础访问控制
     */
    const val BASE = OMRAuth.PERM_OMR_BASE

    /**
     * OMR用户相关权限标识
     * 控制用户管理、用户信息访问等与用户操作相关的权限
     */
    const val USER = OMRAuth.PERM_OMR_USER

    /**
     * OMR游戏相关权限标识
     * 控制游戏创建、游戏管理、游戏数据访问等与游戏业务相关的权限
     */
    const val GAME = OMRAuth.PERM_OMR_GAME
}
