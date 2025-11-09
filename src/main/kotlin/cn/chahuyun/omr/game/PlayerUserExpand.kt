@file:Suppress("SpellCheckingInspection")

package cn.chahuyun.omr.game

import cn.chahuyun.omr.entity.data.PlayerUser


/**
 * 计算玩家升级所需的经验值
 *
 * 该函数根据玩家当前等级计算升级到下一级所需的经验值，
 * 使用立方增长公式来实现经验值需求的递增。
 *
 * @return 升级所需经验值，类型为Long
 */
val PlayerUser.upExp: Long
    get() = if (level == 100) 0L else 103L * (this.level * this.level) + 897L * this.level + 950

/**
 * 获取玩家升级进度的百分比字符串表示
 *
 * @return 格式化后的升级进度百分比字符串，格式为"xx.x%"。
 *         如果所需经验为0或负数，返回"100.0%"；
 *         否则计算当前经验占升级所需经验的百分比，最多显示一位小数，且不超过100%。
 */
val PlayerUser.upp: String
    get() = if (upExp <= 0) {
        "100.0%"
    } else {
        val percentage = this.exp * 100.0 / upExp
        // 限制进度百分比不超过100%，防止显示异常
        val clamped = percentage.coerceAtMost(100.0)
        "%.1f%%".format(clamped)
    }


/**
 * 获取玩家用户的额外攻击力
 *
 * @return 额外攻击力值，默认为0
 */
val PlayerUser.eatk: Long
    get() = 0

/**
 * 计算玩家用户的总攻击力
 * 总攻击力等于基础攻击力与额外攻击力之和
 *
 * @return 玩家用户的总攻击力
 */
val PlayerUser.catk: Long
    get() = this.atk + this.eatk

/**
 * 获取玩家用户的额外防御力
 *
 * @return 额外防御力值，默认为0
 */
val PlayerUser.edef: Long
    get() = 0

/**
 * 计算玩家用户的总防御力
 * 总防御力等于基础防御力与额外防御力之和
 *
 * @return 玩家用户的总防御力
 */
val PlayerUser.cdef: Long
    get() = this.def + this.edef

/**
 * 获取玩家用户的额外生命值
 *
 * @return 额外生命值，默认为0
 */
val PlayerUser.ehp: Long
    get() = 0

/**
 * 计算玩家用户的总生命值
 * 总生命值等于基础生命值与额外生命值之和
 *
 * @return 玩家用户的总生命值
 */
val PlayerUser.chp: Long
    get() = this.hp + this.ehp

/**
 * 获取玩家的额外暴击值
 * @return 返回额外暴击值，默认为0
 */
val PlayerUser.ecrit: Int
    get() = 0

/**
 * 获取玩家的总暴击值
 * @return 返回基础暴击值与额外暴击值的总和
 */
val PlayerUser.ccrit: Int
    get() = (this.crit + this.ecrit)

/**
 * 获取玩家的总暴击伤害值
 *
 * @return 返回玩家的基础暴击伤害加上暴击伤害加成的总和
 */
val PlayerUser.ecritDamage: Int
    get() = 0

/**
 * 获取玩家的暴击伤害加成值
 *
 * @return 返回玩家的暴击伤害加成，固定为0
 */
val PlayerUser.ccritDamage: Int
    get() = this.critDamage + this.ecritDamage


/**
 * 获取玩家用户的额外速度
 *
 * @return 额外速度值，默认为0
 */
val PlayerUser.espeed: Long
    get() = 0

/**
 * 计算玩家用户的总速度
 * 总速度等于基础速度与额外速度之和
 *
 * @return 玩家用户的总速度
 */
val PlayerUser.cspeed: Long
    get() = this.speed + this.espeed


