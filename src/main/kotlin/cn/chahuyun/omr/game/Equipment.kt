package cn.chahuyun.omr.game

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
     * 戒指(左)
     */
    LEFT_RING,

    /**
     * 戒指(右)
     */
    RIGHT_RING
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
     * 是否特殊效果
     */
    val special: Boolean = false
) {
    /**
     * 装备特殊效果
     */
    abstract val effects: List<Effect>

    /**
     * 装备的属性
     */
    abstract val propertyList: List<Property>
}

/**
 * 套装
 */
abstract class Suit(
    val equipmentCodes:List<String>,
    val equipments: List<Equipment>
){
    init {

    }
}
