package cn.chahuyun.omr.game

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
enum class PropertyType {
    HP, ATK, DEF,

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