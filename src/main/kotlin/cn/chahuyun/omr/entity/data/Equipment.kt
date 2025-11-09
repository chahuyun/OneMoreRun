@file:Suppress("JpaDataSourceORMInspection")

package cn.chahuyun.omr.entity.data

import cn.chahuyun.omr.effect.Effect
import cn.chahuyun.omr.entity.PropertyListConverter
import cn.chahuyun.omr.entity.StringListConverter
import cn.chahuyun.omr.equipment.Equipment
import cn.chahuyun.omr.equipment.EquipmentMetadata
import cn.chahuyun.omr.game.Property
import jakarta.persistence.*

/**
 * 随机装备的持久化数据
 */
@Entity
@Table(name = "omr_equipment_metadata")
data class EquipmentRandomData(
    /**
     * 原始code
     */
    @Id
    val code: String,
    /**
     * 数据code
     */
    val metaCode: String,
    /**
     * 对应的效果
     */
    @Transient
    val effects: List<Effect> = emptyList(),
    /**
     * 对应的属性
     */
    @Convert(converter = PropertyListConverter::class)
    val propertyList: List<Property> = emptyList()
){

    fun toMetadata(): EquipmentMetadata {
        return EquipmentMetadata(code,metaCode,effects,propertyList)
    }
}


/**
 * 用户多余装备信息
 */
@Entity
@Table(name = "omr_equipment_backup")
data class EquipmentData(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    var num: Int = 0,
    @Convert(converter = StringListConverter::class) val codes: MutableList<String>
) {
    fun addEquipment(equipment: Equipment) {
        codes.add(equipment.code)
        num = codes.size
    }

    init {
        num = codes.size
    }
}