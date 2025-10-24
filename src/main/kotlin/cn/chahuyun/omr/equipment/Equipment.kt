package cn.chahuyun.omr.game
//Equipment.kt


import cn.chahuyun.omr.effect.Effect

/**
 * 装备类型
 */
enum class EquipmentType {
    /**
     * 头
     */
    HEAD,

    /**
     * 胸
     */
    CHEST,

    /**
     * 手
     */
    HANDS,

    /**
     * 腿
     */
    LEGS,

    /**
     * 脚
     */
    FEET,

    /**
     * 武器
     */
    WEAPONS,

    /**
     * 项链
     */
    NECKLACE,

    /**
     * 戒指
     */
    RING,
}

/**
 * 装备
 */
abstract class Equipment(
    /**
     * 装备code
     */
    val code: String,
    /**
     * 装备名称
     */
    val name: String,
    /**
     * 装备描述
     */
    val description: String,
    /**
     * 装备类型
     */
    val type: EquipmentType,
    /**
     * 套装
     */
    val suit: Suit? = null,
    /**
     * 是否特殊效果
     */
    open val special: Boolean = false,
    /**
     * 是否随机属性
     */
    open val random: Boolean = false,
) : Describable {

    /**
     * 动态生成显示名称
     */
    val displayName: String
        get() = suit?.let { "${it.prefix}$name" } ?: name

    /**
     * 装备特殊效果
     */
    abstract val effects: List<Effect>

    /**
     * 装备的属性
     */
    abstract val propertyList: List<Property>

    /**
     * 随机属性方法，只有在random为true时生效
     */
    open fun generateRandomProperties(): List<Property> = emptyList()

    /**
     * 随机效果方法，只有在random为true时生效
     */
    open fun generateRandomEffects(): List<Effect> = emptyList()

    /**
     * 新装备生成
     */
    fun generateShortId(): String {

        return
    }

}

/**
 * 套装系统
 */
abstract class Suit(
    /**
     * 套装名称
     */
    val name: String,
    /**
     * 前缀
     */
    val prefix: String,
    /**
     * 最低生效件数
     */
    val piecesRequired: Int,
    /**
     * 件数对应的效果
     */
    private val effectsMap: Map<Int, List<Effect>>,
    /**
     * 件数对应的属性
     */
    private val propertyMap: Map<Int, List<Property>>,
) : Describable {

    /**
     * 获取当前装备数量对应的套装效果
     * @param pieceCount 当前装备数量
     * @return 该数量下的套装效果（可能为空）
     */
    fun getEffects(pieceCount: Int): List<Effect> {
        // 找到最大满足件数的效果（如5件套比3件套效果更强）
        return effectsMap.entries.filter { pieceCount >= it.key }.maxByOrNull { it.key } // 优先选择更高件数的效果
            ?.value ?: emptyList()
    }


    /**
     * 获取当前装备数量对应的套装效果
     * @param pieceCount 当前装备数量
     * @return 该数量下的套装效果（可能为空）
     */
    fun getProperty(pieceCount: Int): List<Property> {
        // 找到最大满足件数的效果（如5件套比3件套效果更强）
        return propertyMap.entries.filter { pieceCount >= it.key }.maxByOrNull { it.key } // 优先选择更高件数的效果
            ?.value ?: emptyList()
    }
}