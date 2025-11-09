package cn.chahuyun.omr

/**
 * 装备异常
 */
class EquipmentException(override val message: String) : RuntimeException()

/**
 * 效果异常
 */
class EffectException(override val message: String) : RuntimeException()

/**
 * 技能异常
 */
class SkillsException(override val message: String) : RuntimeException()

/**
 * 副本异常
 */
class DungeonException(override val message: String) : RuntimeException()

/**
 * 游戏流程异常
 */
class GameProcessException(override val message: String) : RuntimeException()