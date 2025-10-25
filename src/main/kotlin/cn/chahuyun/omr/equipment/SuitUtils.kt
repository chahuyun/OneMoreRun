package cn.chahuyun.omr.game

import cn.chahuyun.omr.entity.data.UserEquipment
import cn.chahuyun.omr.equipment.EquipmentFactory
import cn.chahuyun.omr.equipment.Suit

object SuitUtils {
    /**
     * 获取用户装备栏中触发的套装（保底触发）
     * @param userEquipment 用户装备数据
     * @return 满足最低件数的套装列表
     */
    fun getActivatedSuits(userEquipment: UserEquipment): List<ActivatedSuitInfo> {
        // 1. 收集所有装备槽位的装备
        val allEquipment = listOfNotNull(
            userEquipment.head?.let { EquipmentFactory.get(it) },
            userEquipment.chest?.let { EquipmentFactory.get(it) },
            userEquipment.hands?.let { EquipmentFactory.get(it) },
            userEquipment.legs?.let { EquipmentFactory.get(it) },
            userEquipment.feet?.let { EquipmentFactory.get(it) },
            userEquipment.weapons?.let { EquipmentFactory.get(it) },
            userEquipment.necklace?.let { EquipmentFactory.get(it) },
            userEquipment.ring?.let { EquipmentFactory.get(it) },
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