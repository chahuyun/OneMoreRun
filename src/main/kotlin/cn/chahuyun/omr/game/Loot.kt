package cn.chahuyun.omr.game

import cn.chahuyun.omr.util.WeightedRandomUtil
import cn.chahuyun.omr.equipment.EquipmentFactory
import cn.chahuyun.omr.game.LootKey.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 空掉落返回消息code与消息列表
 */
object CodesEmptyLoot {

    const val EMPTY_PROP = "N-empty-1"

    private const val EMPTY_STRING = "没有掉落物!"

    val map = mutableMapOf(
        EMPTY_PROP to EMPTY_STRING
    )


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
     * 掉落数量权重
     */
    val numWeighted: Map<Int, Int>,
    /**
     * 掉落物品权重
     */
    val lootWeighted: List<Pair<Int, Loot>>
) {
    /**
     * 生成掉落物
     */
    fun lootProp(): List<Loot> {
        val n = WeightedRandomUtil.randomByWeight(numWeighted)
        return (1..n).map {
            val loot = WeightedRandomUtil.randomByWeight(lootWeighted)
            when (loot.getLootKey()) {
                EQUIPMENT -> EquipmentFactory.take(loot.code)
                PROP -> TODO() // 假设有药水工厂
                NONE -> EmptyLoot(CodesEmptyLoot.get(loot.code))
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
    Loot(EXP, "EXP")


/**
 * LootHeapBuild类用于构建战利品堆。
 * 该类提供了创建和管理游戏战利品堆的功能。
 */
class LootHeapBuild {

    /**
     * 存储掉落物列表，每个元素为概率权重和掉落物对象的配对
     */
    val loots = mutableListOf<Pair<Int, Loot>>()

    /**
     * 数量映射表，键为概率值，值为对应的数量
     */
    val numMap = mutableMapOf(100 to 1)


    /**
     * 设置权重为100的数值
     * @param num 要设置的数值
     */
    fun num(num: Int) {
        numMap[100] = num
    }

    /**
     * 设置指定权重的数值
     * @param weight 权重值，作为映射的键
     * @param num 要设置的数值，作为映射的值
     */
    fun num(weight: Int, num: Int) {
        numMap[weight] = num
    }


    /**
     * 添加一个金币掉落物
     * @param weight 掉落权重
     * @param value 金币数量
     */
    fun gold(weight: Int, value: Int) {
        val loot = GoldLoot(value)
        loots += weight to loot
    }

    /**
     * 添加一个经验掉落物
     * @param weight 掉落权重
     * @param value 经验值数量
     */
    fun exp(weight: Int, value: Long) {
        val loot = ExpLoot(value)
        loots += weight to loot
    }

    /**
     * 添加一个道具掉落物
     * @param weight 掉落权重
     * @param code 道具编码
     */
    fun prop(weight: Int, code: String) {
        val loot = Loot(LootKey.PROP, code)
        loots += weight to loot
    }

    /**
     * 添加一个装备掉落物
     * @param weight 掉落权重
     * @param code 装备编码
     */
    fun equipment(weight: Int, code: String) {
        val loot = EquipmentFactory.get(code)
        loots += weight to loot
    }

    /**
     * 添加一个空掉落物
     * @param code 空掉落物编码
     */
    fun none(code: String) {
        val loot = EmptyLoot(code)
        loots += 100 to loot
    }

    /**
     * 添加一个空掉落物
     * @param code 空掉落物编码
     */
    fun none(weight: Int) {
        val loot = EmptyLoot(CodesEmptyLoot.EMPTY_PROP)
        loots += weight to loot
    }


    /**
     * 构建掉落物映射表
     * @return 掉落物权重映射表，如果未添加任何掉落物则返回默认的空掉落物
     */
    fun build(): LootHeap {
        if (loots.isEmpty()) {
            loots.clear()
            loots.add(100 to EmptyLoot(CodesEmptyLoot.EMPTY_PROP))
        } else {
            loots.toList()
        }
        return LootHeap(numMap.toMap(), loots)
    }
}


/**
 * LootHeapListBuild类用于构建战利品堆列表
 */
class LootHeapListBuild {

    val heaps = mutableListOf<LootHeap>()

    /**
     * 添加一个战利品堆到列表中
     * @param block 配置LootHeapBuild的lambda表达式，用于设置战利品堆的属性
     */
    fun lootHeap(block: LootHeapBuild.() -> Unit) {
        // 创建LootBuild实例并应用配置块，最后构建结果
        val build = LootHeapBuild()
        build.block()
        heaps.add(build.build())
    }

    /**
     * 构建并返回战利品堆列表
     * @return 包含所有已配置战利品堆的只读列表
     */
    fun build(): List<LootHeap> = heaps.toList()
}


/**
 * 构建战利品映射表 DSL
 *
 * @param block 用于配置LootBuild的lambda表达式
 * @return 返回一个Int到Loot的映射表
 */
fun buildLootHeap(block: LootHeapBuild.() -> Unit): LootHeap {
    // 创建LootBuild实例并应用配置块，最后构建结果
    val build = LootHeapBuild()
    build.block()
    return build.build()
}

/**
 * 构建战利品堆列表的函数 DSL
 *
 * @param block 用于配置LootHeapListBuild对象的lambda表达式，接收LootHeapListBuild作为接收者
 * @return 构建完成的LootHeap列表
 */
fun buildLootHeapList(block: LootHeapListBuild.() -> Unit): List<LootHeap> {
    // 创建LootHeapListBuild实例用于构建过程
    val build = LootHeapListBuild()
    // 执行配置块来设置构建参数
    build.block()
    // 返回最终构建的战利品堆列表
    return build.build()
}

