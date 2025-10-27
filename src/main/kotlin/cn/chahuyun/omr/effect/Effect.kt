@file:Suppress("unused")

package cn.chahuyun.omr.effect
//Effect.kt


import cn.chahuyun.omr.entity.GameEntity
import cn.chahuyun.omr.game.Describable
import cn.chahuyun.omr.game.DungeonGameEntity
import cn.chahuyun.omr.game.GameProcess
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

//Effect.kt

/**
 * 效果类型
 */
enum class EffectType {
    BUFF,

    /**
     * 唯一效果
     */
    ONLY_BUFF,
    DEBUFF,

    /**
     * 唯一效果
     */
    ONLY_DEBUFF
}

/**
 * 效果触发时机
 */
enum class Trigger {
    /**
     * 副本开启时
     */
    ON_DUNGEON_START,

    /**
     * 单体回合开始时
     */
    ON_TURN_START,

    /**
     * 技能释放时
     */
    ON_SKILL_CAST,

    /**
     * 伤害计算时
     */
    ON_DAMAGE_CALCULATE,
    //todo 这里是不是应该还有一个时刻,用于给类似中毒效果使用
    /**
     * 单体回合结算时
     */
    ON_TURN_SETTLE,

    /**
     * 副本结束时
     */
    ON_DUNGEON_END
}

/**
 * 效果
 *
 */
@Serializable
abstract class Effect(
    /**
     * 效果code
     */
    val code: String,
    /**
     * 效果名称
     */
    val name: String,
    /**
     * 效果类型
     */
    val type: EffectType,
    /**
     * 触发时机
     */
    val trigger: Trigger,
    /**
     * 剩余回合
     */
    val duration: Int = 0,
    /**
     * 计算优先级
     */
    val priority: Int = 100,
    /**
     * 是否造成数值影响（如伤害或治疗）
     */
    val onNumericImpact: Boolean = false,
    /**
     * 效果来源,默认为[DungeonGameEntity]
     */
    @Transient
    val source: GameEntity = DungeonGameEntity()
) : Describable {

    /**
     * 数值
     */
    abstract var value: Float
        internal set

    /**
     * 添加时效果
     */
    open fun onApply(entity: GameEntity, process: GameProcess) {}

    /**
     * 删除时效果
     */
    open fun onRemove(entity: GameEntity, process: GameProcess) {}

    /**
     * 计算时效果
     */
    open fun onTurn(entity: GameEntity, process: GameProcess) {}

    /**
     * 造成伤害或治疗
     */
    open fun applyImpact(entity: GameEntity, process: GameProcess) {}

    /**
     * 遇见唯一效果的处理方式
     */
    open fun merge(entity: GameEntity, effect: Effect) {}
}