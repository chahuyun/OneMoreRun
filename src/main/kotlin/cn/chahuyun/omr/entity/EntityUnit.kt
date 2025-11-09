package cn.chahuyun.omr.entity
//EntityUnit.kt

import cn.chahuyun.omr.effect.Effect
import cn.chahuyun.omr.game.BaseProperty
import cn.chahuyun.omr.game.Impact
import cn.chahuyun.omr.skills.Skills


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
abstract class GameEntity() : EntityUnit, BaseProperty() {

    /**
     * 当前hp
     */
    var currentHp: Long = 0

    /**
     * 当前atk
     */
    var currentAtk: Long = 0

    /**
     * 当前防御
     */
    var currentDef: Long = 0

    /**
     * 当前暴击概率(%)
     */
    var currentCrit: Int = 0

    /**
     * 当前暴击伤害(%)
     */
    var currentCritDamage: Int = 0

    /**
     * 拥有的技能列表
     */
    val skills = mutableListOf<Skills>()

    /**
     *  当前应用的所有效果
     */
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
 *
 * @property name 实体名称
 * @property description 实体描述
 * @property speed 初始速度
 * @property currentSpeed 当前速度
 * @property atk 攻击力
 * @property def 防御力
 * @property hp 生命值
 * @property crit 暴击概率(%)
 * @property critDamage 暴击伤害(%)
 */
class Player(
    override val name: String,
    override val description: String,
    /**
     * 初始速度
     */
    val speed: Int,
    override val atk: Long,
    override val def: Long,
    override val hp: Long,
    override val crit: Int,
    override val critDamage: Int
) : GameEntity() {
    /**
     * 当前速度
     */
    var currentSpeed: Int

    // 初始化当前属性值
    init {
        currentHp = hp
        currentAtk = atk
        currentDef = def
        currentCrit = crit
        currentCritDamage = critDamage
        currentSpeed = speed
    }
}

/**
 * boss实体
 *
 * @property name 实体名称
 * @property description 实体描述
 * @property atk 攻击力
 * @property def 防御力
 * @property hp 生命值
 * @property crit 暴击概率(%)
 * @property critDamage 暴击伤害(%)
 */
class Boss(
    override val name: String,
    override val description: String,
    override val atk: Long,
    override val def: Long,
    override val hp: Long,
    override val crit: Int,
    override val critDamage: Int
) : GameEntity() {
    // 初始化当前属性值
    init {
        currentHp = hp
        currentAtk = atk
        currentDef = def
        currentCrit = crit
        currentCritDamage = critDamage
    }
}
