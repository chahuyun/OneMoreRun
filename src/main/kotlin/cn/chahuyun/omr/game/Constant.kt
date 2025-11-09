package cn.chahuyun.omr.game

/**
 * 常量类
 */
object Contstant {

    /**
     * 获取随机攻击字符串
     *
     * @return 随机返回"攻击"或"平a"字符串
     */
    val ATTACK_STRING
        get() = listOf("攻击", "平a").random()


}