package cn.chahuyun.omr.game
//Damage.kt

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
 * 伤害
 */
data class Damage(
    /**
     * 基础伤害
     */
    val baseDamage: Int,
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
     * 最终伤害
     */
    var finalDamage: Int = 0
) {
    init {
        /**
         * 初始伤害
         */
        finalDamage = baseDamage
    }
}