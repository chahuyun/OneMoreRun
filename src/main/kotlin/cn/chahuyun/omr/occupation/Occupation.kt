package cn.chahuyun.omr.occupation

import cn.chahuyun.omr.occupation.OccupationType.*
import kotlinx.serialization.Serializable

/**
 * 职业类型枚举类
 *
 * 定义了游戏中角色的职业类型，包括坦克、输出、支援和治疗四种基础职业
 */
@Serializable
enum class OccupationType(val type: String) {
    /**
     * 坦克职业 - 主要负责承受伤害和保护队友
     */
    TANK("TANK"),

    /**
     * 输出职业 - 主要负责造成伤害
     */
    DPS("DPS"),

    /**
     * 支援职业 - 主要负责提供辅助和控制
     */
    SUPPORT("Support"),

    /**
     * 治疗职业 - 主要负责恢复队友生命值
     */
    HEALER("Healer")
}


/**
 * 职业枚举类
 *
 * 定义了游戏中的各种职业及其对应的类型分类
 * 每个职业包含显示名称和职业类型两个属性
 */
@Serializable
enum class Occupation(val occ: String, val type: OccupationType) {
    // 坦克类型职业
    PALADIN("圣骑士", TANK),
    WARRIOR("战士", TANK),
    MONK("武僧", TANK),

    // 输出类型职业
    ARCHER("弓箭手", DPS),
    MAGE("魔法师", DPS),
    ASSASSIN("刺客", DPS),

    // 支援类型职业
    BARD("诗人", SUPPORT),
    HUNTER("猎人", SUPPORT),
    WARLOCK("术士", SUPPORT),

    // 治疗类型职业
    PRIEST("牧师", HEALER),
    SHAMAN("萨满", HEALER),
    DRUID("德鲁伊", HEALER),
}
