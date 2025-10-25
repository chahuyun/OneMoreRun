@file:Suppress("unused")

package cn.chahuyun.omr.equipment

import cn.chahuyun.omr.entity.data.UserEquipment

object SuitUtils {
    /**
     * 获取用户装备栏中触发的套装（保底触发）
     * @param userEquipment 用户装备数据
     * @return 满足最低件数的套装列表
     */
    fun getActivatedSuits(userEquipment: UserEquipment): List<ActivatedSuitInfo> {
        // 1. 收集所有装备槽位的装备
        val allEquipment = listOfNotNull(
            userEquipment.head?.let { EquipmentFactory.take(it) },
            userEquipment.chest?.let { EquipmentFactory.take(it) },
            userEquipment.hands?.let { EquipmentFactory.take(it) },
            userEquipment.legs?.let { EquipmentFactory.take(it) },
            userEquipment.feet?.let { EquipmentFactory.take(it) },
            userEquipment.weapons?.let { EquipmentFactory.take(it) },
            userEquipment.necklace?.let { EquipmentFactory.take(it) },
            userEquipment.ring?.let { EquipmentFactory.take(it) },
        )

        // 2. 统计每个套装的装备数量和具体装备code
        val suitEquipmentMap = mutableMapOf<Suit, MutableList<String>>()
        allEquipment.forEach { equipment ->
            equipment.suit?.let { suit ->
                suitEquipmentMap.getOrPut(suit) { mutableListOf() }.add(equipment.code)
            }
        }

        // 3. 过滤出满足最低件数的套装
        return suitEquipmentMap.filter { (suit, codes) -> codes.size >= suit.piecesRequired }
            .map { (suit, codes) -> ActivatedSuitInfo(suit, codes) }
            .toList()
    }
}