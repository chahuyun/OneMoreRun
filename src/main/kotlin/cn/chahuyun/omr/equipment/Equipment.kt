@file:Suppress("unused")

package cn.chahuyun.omr.equipment
//Equipment.kt


import cn.chahuyun.omr.effect.Effect
import cn.chahuyun.omr.game.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

/**
 * 装备类型
 */
enum class EquipmentSlot {
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
@Serializable
abstract class Equipment(
    /**
     * 装备code
     */
    override val code: String,
    /**
     * 装备名称
     */
    val name: String,
    /**
     * 装备描述
     */
    val description: String,
    /**
     * 装备部位
     */
    val slot: EquipmentSlot,
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
) : Loot(LootKey.EQUIPMENT, code), Describable, BaseProp {

    /**
     * 装备类型标识 - 用于序列化鉴别
     */
    val equipmentType: String = this::class.simpleName ?: this::class.java.simpleName

    /**
     * 动态生成显示名称
     */
    val displayName: String
        get() = suit?.let { "${it.prefix}$name" } ?: name

    /**
     * 装备特殊效果
     */
    val effects: List<Effect> by lazy { generateEffects.invoke() }

    /**
     * 装备的属性
     */
    val propertyList: List<Property> by lazy { generateProperties.invoke() }

    /**
     * 生成属性方法
     */
    abstract val generateEffects: () -> List<Effect>

    /**
     *  生成效果方法
     */
    abstract val generateProperties: () -> List<Property>

    internal fun setEffects(effects: List<Effect>) {
        try {
            val field = this.javaClass.getDeclaredField("effects")
            field.isAccessible = true
            field.set(this, effects)
        } catch (e: Exception) {
            throw RuntimeException("Failed to set effects", e)
        }
    }

    internal fun setPropertyList(propertyList: List<Property>) {
        try {
            val field = this.javaClass.getDeclaredField("propertyList")
            field.isAccessible = true
            field.set(this, propertyList)
        } catch (e: Exception) {
            throw RuntimeException("Failed to set propertyList", e)
        }
    }

    internal fun setCode(code: String) {
        try {
            val field = this.javaClass.getDeclaredField("code")
            field.isAccessible = true
            field.set(this, code)
        } catch (e: Exception) {
            throw RuntimeException("Failed to set code", e)
        }
    }

    companion object {

        /**
         * 优雅的克隆方法
         */
        internal inline fun <reified T : Equipment> T.clone(): T {
            return JsonConfig.jsonFormat.decodeFromString<T>(
                JsonConfig.jsonFormat.encodeToString(this)
            )
        }
    }
}

/**
 * 套装系统
 */
@Serializable
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

