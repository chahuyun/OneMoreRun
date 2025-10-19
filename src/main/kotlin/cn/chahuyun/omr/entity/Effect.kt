package cn.chahuyun.omr.entity
//Effect.kt


object EffectType {
    enum class Type { BUFF, DEBUFF }
}

/**
 * 效果
 */
abstract class Effect(
    /**
     * 效果名称
     */
    val name: String,
    /**
     * 剩余回合
     */
    val duration: Int,
    /**
     * 计算优先级
     */
    val priority: Int,
){
    /**
     * 数值
     */
    abstract val value: Float

    /**
     * 添加时效果
     */
    open fun onApply(entity: GameEntity) {}

    /**
     * 删除时效果
     */
    open fun onRemove(entity: GameEntity) {}

    /**
     * 计算时效果
     */
    open fun onTurn(entity: GameEntity) {}

    /**
     * 这个是什么?
     */
    open fun isBetterThan(other: Effect): Boolean = value > other.value
}