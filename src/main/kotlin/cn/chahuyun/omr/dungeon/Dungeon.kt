package cn.chahuyun.omr.dungeon

import cn.chahuyun.omr.game.BaseProperty
import cn.chahuyun.omr.game.Loot

/**
 *  副本，表示游戏副本的基本信息和Boss配置
 *
 * @param code 副本唯一标识码
 * @param name 副本显示名称
 * @param bossName Boss角色名称
 * @param bossDescription Boss角色描述信息
 */
abstract class Dungeon(
    /**
     * 副本code
     */
    val code: String,
    /**
     * 副本名称
     */
    val name: String,
    /**
     * boss名称
     */
    val bossName: String,
    /**
     * boss描述
     */
    val bossDescription: String
) {
    /**
     * 不同等级的boss属性
     * @return Map<Int, BaseProperty> 返回不同等级对应的boss属性映射
     */
    abstract val bossProperty: Map<Int, BaseProperty>

    /**
     * boss拥有技能 code列表
     * @return Boss角色的技能列表，键为等级，值为技能code列表
     */
    abstract val skills: Map<Int, List<String>>

    /**
     * 副本掉落物品
     * @return Map<Int, List<String>> 返回不同等级副本的掉落物品映射
     */
    abstract val dropProp: Map<Int, List<Loot>>

}
