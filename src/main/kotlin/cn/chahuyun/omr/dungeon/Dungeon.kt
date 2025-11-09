package cn.chahuyun.omr.dungeon

import cn.chahuyun.omr.GameProcessException
import cn.chahuyun.omr.entity.Boss
import cn.chahuyun.omr.game.BaseProperty
import cn.chahuyun.omr.game.Loot
import cn.chahuyun.omr.game.LootHeap

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
    abstract val dropLoot: Map<Int, List<LootHeap>>

    /**
     * 获取必定掉落的战利品列表
     *
     * @return 包含所有必定掉落物品的列表，每个物品都将在战斗或事件结束后无条件获得
     */
    abstract val certainlyLoot: Map<Int, List<Loot>>

    companion object {
        /**
         * 将地下城转换为对应难度的Boss对象
         *
         * @param difficulty 指定的难度等级，用于获取对应的Boss属性
         * @return 根据指定难度创建的Boss对象，包含完整的战斗属性
         * @throws GameProcessException 当指定难度不存在于bossProperty映射中时抛出异常
         */
        fun Dungeon.toBoss(difficulty: Int): Boss {
            // 获取指定难度对应的Boss属性，如果不存在则抛出异常
            val property = bossProperty[difficulty] ?: throw GameProcessException("游戏流程异常:该副本没有这个难度!")
            // 根据Boss属性创建并返回Boss对象
            return Boss(
                name,
                bossDescription,
                property.atk,
                property.def,
                property.hp,
                property.crit,
                property.critDamage
            )
        }
    }
}
