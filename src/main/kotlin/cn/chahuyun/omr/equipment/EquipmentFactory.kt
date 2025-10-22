package cn.chahuyun.omr.equipment

import cn.chahuyun.omr.EquipmentException
import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.game.Equipment

object EquipmentFactory {

    private val equipmentMap: MutableMap<String, Equipment> = mutableMapOf()

    /**
     * 获取装备
     */
    fun get(code: String): Equipment {
        return equipmentMap[code] ?: throw EquipmentException("对应code装备未注册!")
    }

    /**
     * 注册装备
     */
    fun register(code: String, equipment: Equipment) {
        if (equipmentMap.containsKey(code)) {
            OneMoreRun.logger.warning("$code 已被注册,新注册失败!")
            return
        }
        equipmentMap[code] = equipment
    }

}