package cn.chahuyun.omr.game

enum class PropertyType{
     HP,ATK,DEF,

    /**
     * 暴击
     */
    DAMAGE,

    /**
     * 爆伤
     */
    CRIT_DAMAGE
}

/**
 * 某项数值
 */
class Property(
    /**
     * 值,暴击爆伤为%
     */
    var value: Int,
    val type: PropertyType
)