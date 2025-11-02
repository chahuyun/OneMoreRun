package cn.chahuyun.omr.game

import cn.chahuyun.omr.WeightedRandomUtil
import cn.chahuyun.omr.equipment.EquipmentFactory

/**
 * 空掉落返回消息code与消息列表
 */
object LootEmptyPropCodes {

    const val EMPTY_PROP = "N-empty-1"

    val map = mutableMapOf(
        EMPTY_PROP to EMPTY_STRING
    )

    private const val EMPTY_STRING = "没有掉落物!"

    fun get(code: String): String {
        return map[code] ?: EMPTY_STRING
    }

}


/**
 * 掉落物
 */
class Loot(
    /**
     * 掉落物权重
     */
    val weight: Int,
    /**
     * 掉落列表
     * 权重 to 物品code
     */
    val lootProp: Map<Int, String>,
    /**
     * 掉落数量
     */
    val num: Int = 1
) {

    /**
     * 生成掉落物
     */
    fun lootProp(): List<BaseProp> {
        return (1..num).map {
            val propCode = WeightedRandomUtil.randomByWeight(lootProp)
            when (propCode.firstOrNull()) {
                'E' -> EquipmentFactory.take(propCode)
                'P' -> TODO() // 假设有药水工厂
                'N' -> EmptyProp(LootEmptyPropCodes.get(propCode))
                else -> throw IllegalArgumentException("掉落物代码异常: $propCode")
            }
        }
    }
}


/**
 * 空道具类，用于表示一个基本的道具实现
 * @param message 道具消息内容
 * @param code 道具编码，默认值为"N"
 */
class EmptyProp(message: String, override val code: String = "N") : BaseProp {
}
