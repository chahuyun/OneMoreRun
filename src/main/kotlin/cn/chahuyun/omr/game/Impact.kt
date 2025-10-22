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
    val attacker: GameEntity,
    /**
     * 伤害目标
     */
    val objectives: List<GameEntity>,
    /**
     * 是否为治疗
     */
    val isTreatment: Boolean,
    /**
     * 伤害类型
     */
    val damageType: DamageType,
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