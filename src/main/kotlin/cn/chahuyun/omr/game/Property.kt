@file:Suppress("unused")

package cn.chahuyun.omr.game

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
enum class PropertyType {
    HP, ATK, DEF, SPEED,

    /**
     * 暴击
     */
    CRIT,

    /**
     * 爆伤
     */
    CRIT_DAMAGE
}

/**
 * 某项数值
 */
@Serializable
data class Property(
    /**
     * 值,暴击爆伤为%
     */
    var value: Int,
    val type: PropertyType,
) {
    companion object {
        /**
         * 随机属性
         * @param min 最小值
         * @param max 最大值
         */
        fun random(min: Int, max: Int, type: PropertyType): Property {
            return Property(Random.nextInt(min, max + 1), type)
        }

        /**
         * 随机属性,在属性列表中随机选择一个
         * @param min 最小值
         * @param max 最大值
         */
        fun random(min: Int, max: Int, vararg types: PropertyType): Property {
            return Property(Random.nextInt(min, max + 1), types.random())
        }

        /**
         * 随机属性,在属性列表中随机选择 typeNum 个
         * @param min 最小值
         * @param max 最大值
         * @param typeNum 选择的属性类型
         */
        fun random(min: Int, max: Int, typeNum: Int, vararg types: PropertyType): List<Property> {
            return types.toList().shuffled().take(typeNum).map {
                Property(Random.nextInt(min, max + 1), it)
            }
        }

        /**
         * 随机属性类型，值固定，在类型中随机选择一个
         */
        fun randomType(value: Int, vararg types: PropertyType): Property {
            return Property(value, types.random())
        }

        /**
         * 随机属性类型，值固定，在类型中随机选择一个
         */
        fun randomType(value: Int, typeNum: Int, vararg types: PropertyType): List<Property> {
            return types.toList().shuffled().take(typeNum).map { Property(value, it) }
        }

    }
}

/**
 * 基本属性
 */
abstract class BaseProperty {
    /**
     * 血量
     */
    abstract val hp: Long

    /**
     * 攻击
     */
    abstract val atk: Long

    /**
     * 防御
     */
    abstract val def: Long

    /**
     * 暴击概率:%
     */
    abstract val crit: Int

    /**
     * 爆伤%
     */
    abstract val critDamage: Int

    companion object {
        /**
         * 创建一个基础属性对象
         *
         * @param hp 生命值属性值
         * @param atk 攻击力属性值
         * @param def 防御力属性值
         * @param crit 暴击率属性值，默认为0
         * @param critDamage 暴击伤害属性值，默认为100
         * @return 返回一个实现了BaseProperty接口的匿名对象，包含指定的属性值
         */
        fun create(
            hp: Long,
            atk: Long,
            def: Long,
            crit: Int = 0,
            critDamage: Int = 100
        ): BaseProperty = object : BaseProperty() {
            override val hp: Long = hp
            override val atk: Long = atk
            override val def: Long = def
            override val crit: Int = crit
            override val critDamage: Int = critDamage
        }
    }
}