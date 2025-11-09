@file:Suppress("unused")

package cn.chahuyun.omr.equipment
//EquipmentFactory.kt

import cn.chahuyun.hibernateplus.HibernateFactory
import cn.chahuyun.omr.EquipmentException
import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.effect.Effect
import cn.chahuyun.omr.entity.data.EquipmentRandomData
import cn.chahuyun.omr.equipment.Equipment.Companion.clone
import cn.chahuyun.omr.equipment.EquipmentFactory.take
import cn.chahuyun.omr.game.Property
import cn.chahuyun.omr.util.ShortIdGenerator
import net.mamoe.mirai.utils.debug

object EquipmentFactory {
    private val log = OneMoreRun.logger

    private val equipmentMap: MutableMap<String, Equipment> = mutableMapOf()
    private val randomEquipmentMap: MutableMap<String, Equipment> = mutableMapOf()

    init {
        // 初始化JSON配置（触发类扫描）
        JsonConfig.jsonFormat
    }

    fun loader() {
        val list = HibernateFactory.selectList(EquipmentRandomData::class.java)
        for (data in list) {
            try {
                randomEquipmentMap[data.metaCode] = deserialize(data.toMetadata())
            } catch (e: Exception) {
                log.error("反序列化装备错误:${data.metaCode}", e)
                continue
            }
        }
    }

    /**
     * 注册装备原型
     */
    fun register(equipment: Equipment) {
        val code = equipment.code
        if (equipmentMap.containsKey(code)) {
            log.warning("$code 已被注册,新注册失败!")
            return
        }
        equipmentMap[code] = equipment
        JsonConfig.registerEquipmentClass(equipment::class)
        log.debug { "注册装备: $code (${equipment::class.simpleName})" }
    }

    /**
     * 获取装备原型,需要获取实际装备请使用 [take]
     */
    fun get(code: String): Equipment {
        return equipmentMap[code] ?: throw EquipmentException("对应code: $code 装备未注册!")
    }

    /**
     * 获取装备,没有就创建,有就返回
     */
    fun take(code: String): Equipment {
        if (randomEquipmentMap.containsKey(code)) {
            return randomEquipmentMap[code] ?: throw EquipmentException("该随机装备未反序列化")
        }

        val equipment = get(code)
        return if (equipment.random) {
            val clone = equipment.clone()
            val finalCode = "$code-${ShortIdGenerator.generateShortId()}-R"
            clone.propertyList
            clone.effects
            HibernateFactory.merge(serialize(finalCode, clone).toData())
            randomEquipmentMap[finalCode] = clone
            clone.setCode(finalCode)
            clone
        } else {
            equipment.propertyList
            equipment.effects
            equipment
        }
    }

    // 序列化：只保存 Metadata
    fun serialize(code: String, equipment: Equipment): EquipmentMetadata {
        // 触发 lazy 初始化（不修改装备状态）
        val effects = equipment.effects
        val propertyList = equipment.propertyList
        return EquipmentMetadata(
            code = equipment.code,
            metaCode = code,
            effects = effects,
            propertyList = propertyList
        )
    }

    // 反序列化：从 Metadata 恢复装备
    fun deserialize(metadata: EquipmentMetadata): Equipment {
        //从注册表获取基础装备
        val base = get(metadata.code)

        val metaCode = metadata.metaCode
        val newEquipment = base.clone().apply {
            // 用 Metadata 中的值覆盖属性（通过 internal 方法）
            setCode(metaCode)
            setEffects(metadata.effects)
            setPropertyList(metadata.propertyList)
        }

        return newEquipment
    }


    /**
     * 重新加载序列化配置（用于热重载）
     */
    fun refreshSerialization() {
        JsonConfig.refresh()
    }
}

/**
 * 装备元数据
 */
data class EquipmentMetadata @JvmOverloads constructor(
    /**
     * 原始code
     */
    val code: String,
    /**
     * 数据code
     */
    val metaCode: String,
    /**
     * 对应的效果
     */
    val effects: List<Effect> = emptyList(),
    /**
     * 对应的属性
     */
    val propertyList: List<Property> = emptyList()
) {
    fun toData(): EquipmentRandomData {
        return EquipmentRandomData(code, metaCode, effects, propertyList)
    }
}