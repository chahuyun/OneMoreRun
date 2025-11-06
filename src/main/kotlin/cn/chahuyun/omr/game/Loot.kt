package cn.chahuyun.omr.game

import cn.chahuyun.omr.WeightedRandomUtil
import cn.chahuyun.omr.equipment.EquipmentFactory
import cn.chahuyun.omr.game.LootKey.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
 * 战利品类型枚举类
 *
 * 定义游戏中可能出现的战利品类型，用于区分不同类型的奖励物品
 */
@Serializable
enum class LootKey {
    NONE,      //没有掉落物
    GOLD,      // 金币奖励类型
    EXP,       // 经验值奖励类型
    EQUIPMENT, // 装备奖励类型
    PROP       // 道具奖励类型
}


/**
 * LootKeyApi接口定义了获取LootKey的API接口
 */
interface LootKeyApi {
    /**
     * 获取LootKey对象
     *
     * @return LootKey 返回一个LootKey实例
     */
    fun getLootKey(): LootKey
}

class LootHeap(
    /**
     * 掉落堆物权重
     */
    val weight: Int,
    /**
     * 掉落列表
     * 权重 to 掉落物
     */
    val lootProp: Map<Int, Loot>,
    /**
     * 掉落数量
     */
    val num: Int = 1
) {
    /**
     * 生成掉落物
     */
    fun lootProp(): List<Loot> {
        return (1..num).map {
            val loot = WeightedRandomUtil.randomByWeight(lootProp)
            when (loot.getLootKey()) {
                EQUIPMENT -> EquipmentFactory.take(loot.code)
                PROP -> TODO() // 假设有药水工厂
                NONE -> EmptyLoot(LootEmptyPropCodes.get(loot.code))
                GOLD -> loot as GoldLoot
                EXP -> loot as ExpLoot
            }
        }
    }
}

/**
 * 掉落物
 */
@Serializable
open class Loot(
    val key: LootKey = NONE,
    @SerialName("lootCode") override val code: String
) : LootKeyApi, BaseProp {
    /**
     * 获取LootKey对象
     *
     * @return LootKey 返回一个LootKey实例
     */
    override fun getLootKey(): LootKey = key
}


/**
 * 空掉落物
 * @param message 道具消息内容
 * @param code 道具编码，默认值为"N"
 */
@Serializable
class EmptyLoot(val message: String) : Loot(code = "N")


/**
 * 金币战利品类
 *
 * @param value 金币数量，默认为0
 * @param message 战利品消息描述
 * @param code 战利品代码，默认为"GOLD"
 */
class GoldLoot(value: Int = 0, message: String = "你获得了 $value 的金币") :
    Loot(GOLD, code = "GOLD")

/**
 * 经验值战利品类
 *
 * @param value 经验值数量，默认为0L
 * @param message 战利品消息描述
 * @param code 战利品代码，默认为"EXP"
 */
class ExpLoot(value: Long = 0L, message: String = "你获得了 $value 的经验") :
    Loot(EXP,  "EXP")


