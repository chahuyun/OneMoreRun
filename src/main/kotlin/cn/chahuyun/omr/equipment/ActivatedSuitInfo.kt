@file:Suppress("unused")

package cn.chahuyun.omr.equipment

import cn.chahuyun.omr.effect.Effect
import cn.chahuyun.omr.game.Property

/**
 * 已激活套装的详细信息
 * @param suit 触发的套装
 * @param equippedCodes 当前装备的装备code列表
 */
data class ActivatedSuitInfo(
    val suit: Suit,
    val equippedCodes: List<String>
) {
    /**
     * 当前装备数量
     */
    val count: Int get() = equippedCodes.size

    /**
     * 获取套装效果（根据当前装备数量）
     */
    fun getEffects(): List<Effect> = suit.getEffects(count)

    /**
     * 获取套装属性（根据当前装备数量）
     */
    fun getProperty(): List<Property> = suit.getProperty(count)
}