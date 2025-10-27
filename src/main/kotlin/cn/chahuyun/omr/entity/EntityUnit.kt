package cn.chahuyun.omr.entity
//EntityUnit.kt

import cn.chahuyun.omr.effect.Effect
import cn.chahuyun.omr.game.Impact
import kotlinx.serialization.Serializable


/**
 * 实体单位
 */
interface EntityUnit {
    /**
     * 实体名称
     */
    val name: String

    /**
     * 实体描述
     */
    val description: String
}

/**
 * 游戏实体
 */
abstract class GameEntity : EntityUnit {
    /**
     * 初始hp
     */
    open val hp: Int = 0

    /**
     * 当前hp
     */
    var currentHp: Int = 0

    /**
     * 初始atk
     */
    val atk: Int = 0

    /**
     * 当前atk
     */
    var currentAtk: Int = 0

    /**
     * 初始防御
     */
    val def: Int = 0

    /**
     * 当前防御
     */
    var currentDef: Int = 0

    /**
     * 初始暴击概率(%)
     */
    val crit: Int = 0

    /**
     * 当前暴击概率(%)
     */
    var currentCrit: Int = 0

    /**
     * 初始暴击伤害(%)
     */
    val critDamage: Int = 0

    /**
     * 当前暴击伤害(%)
     */
    var currentCritDamage: Int = 0


    // 当前应用的所有效果
    val effects = mutableListOf<Effect>()

    /**
     * 造成的伤害
     */
    val damageDealt = mutableListOf<Impact>()

    /**
     * 受到的伤害
     */
    val damageTaken = mutableListOf<Impact>()
}

/**
 * 玩家实体
 */
class Player(
    override val name: String,
    override val description: String,
    /**
     * 初始速度
     */
    val speed: Int,
    /**
     * 当前速度
     */
    var currentSpeed: Int
) : GameEntity() {}

/**
 * boss实体
 */
class Boss(override val name: String, override val description: String) : GameEntity()