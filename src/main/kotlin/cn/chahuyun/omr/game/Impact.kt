@file:Suppress("unused")

package cn.chahuyun.omr.game
//Impact.kt

import cn.chahuyun.omr.entity.GameEntity

/**
 * 伤害类型
 */
enum class DamageType {
    /**
     * 物理
     */
    PHYSICAL,

    /**
     * 魔法
     */
    MAGIC,

    /**
     * 真伤
     */
    PURE
}

/**
 * 数值影响 / 伤害或治疗
 */
data class Impact(
    /**
     * 基础数值
     */
    val baseValue: Int,
    /**
     * 伤害来源
     */
    val source: GameEntity,
    /**
     * 伤害目标
     */
    val targets: List<GameEntity>,
    /**
     * 伤害类型
     */
    var damageType: DamageType,
    /**
     * 是否为治疗
     */
    val isTreatment: Boolean = false,
    /**
     * 最终数值
     */
    var finalValue: Int = 0
) {
    init {
        /**
         * 初始数值
         */
        finalValue = baseValue
    }
}

data class ImpactConfig(
    /**
     * 伤害类型
     */
    val damageType: DamageType,
    /**
     * 基本伤害计算
     */
    val calculateBaseValue: (self: GameEntity, target: List<GameEntity>) -> Int
)