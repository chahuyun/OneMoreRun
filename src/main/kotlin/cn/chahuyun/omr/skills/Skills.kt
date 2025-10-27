@file:Suppress("unused")

package cn.chahuyun.omr.skills

import cn.chahuyun.omr.entity.GameEntity
import cn.chahuyun.omr.game.Describable
import cn.chahuyun.omr.game.ImpactConfig


enum class SkillsType {
    /**
     * 职业技能
     */
    CLASS,

    /**
     * 自定义技能
     */
    CUSTOM,

    /**
     * 被动技能
     */
    PASSIVE
}

/**
 * 定义技能、效果或交互的目标选择类型。
 *
 * 该枚举用于指定游戏内各种机制（如技能、治疗、伤害、增益/减益）的作用目标。
 * 使用 [enum class] 提供类型安全和命名空间隔离。
 * 每个枚举值都附带清晰的注释，说明其目标范围、是否包含自己以及典型使用场景。
 */
enum class TargetType {
    /**
     * 目标为施法者自身。
     *
     * 用于自身增益（如加速、护盾）、自我治疗或消耗性技能。
     * 范围：仅自己。
     */
    SELF,

    /**
     * 目标为除自己外的任意一位队友。
     *
     * 用于单体治疗、单体增益、救援技能等。
     * 范围：队友，不包含自己。
     */
    ALLY,

    /**
     * 目标为除自己外的两位队友。
     *
     * 用于小范围群体治疗、双人增益或特定组合技能。
     * 范围：队友，数量为2，不包含自己。
     */
    ALLIES_TWO,

    /**
     * 目标为所有玩家，包括自己和所有队友。
     *
     * 用于全队增益、全队治疗、全队复活等团队级效果。
     * 范围：所有玩家单位。
     */
    ALL_PLAYERS,

    /**
     * 目标为游戏中的Boss敌人。
     *
     * 用于专门针对首领的技能、减益或特殊机制。
     * 范围：敌方Boss单位。
     */
    BOSS,

    /**
     * 目标为队伍中的DPS（输出位）角色。
     *
     * 用于保护输出、强化输出或救援核心输出位。
     * 范围：所有被标记为DPS的队友（可能包含自己）。
     */
    DPS,

    /**
     * 目标为队伍中的Tank（扛伤位）角色。
     *
     * 用于治疗坦克、强化坦克防御或保护前排。
     * 范围：所有被标记为Tank的队友（可能包含自己）。
     */
    TANK,

    /**
     * 目标为生命值未满的单位（处于“受伤”状态）。
     *
     * 用于治疗技能、急救效果。目标可以是自己或队友。
     * 范围：所有生命值 < 最大生命值的友方单位。
     */
    INJURED,

    /**
     * 目标为生命值极低的单位（处于“濒死”状态）。
     *
     * 用于紧急治疗、斩杀或救场技能。通常设定血量阈值（如 < 25%）。
     * 范围：所有生命值低于危险阈值的友方单位。
     */
    LOW_HEALTH,

    /**
     * 目标为当前拥有增益效果（Buff）的单位。
     *
     * 用于强化已有增益、触发联动效果或保护关键增益。
     * 范围：所有带有任意增益状态的友方单位（可能包含自己）。
     */
    BUFFED,

    /**
     * 目标为当前拥有减益效果（Debuff）的单位。
     *
     * 用于驱散减益、对被弱化目标追加伤害或触发特效。
     * 范围：所有带有任意减益状态的单位（可为敌方或友方，依技能而定）。
     */
    DEBUFFED
}

abstract class Skills(
    /**
     * 技能code
     */
    val code: String,
    /**
     * 技能名称
     */
    val name: String,
    /**
     * 技能描述
     */
    val description: String,
    /**
     * 技能类型
     */
    val skillsType: SkillsType,
    /**
     * 目标类型
     */
    val target: TargetType,
    /**
     * 冷却回合
     */
    val cooldown: Int = 0,
) : Describable {
    /**
     * 效果codes
     */
    abstract fun getEffectCodes(): List<String>

    /**
     * 自定义伤害配置
     */
    abstract fun getImpactConfigs(): List<ImpactConfig>

    /**
     * 使用技能
     * 暂时忽略,还没想好设计
     */
    fun use(self: GameEntity, target: List<GameEntity>, process: Process) {}

}