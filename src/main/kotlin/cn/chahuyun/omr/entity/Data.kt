@file:Suppress("JpaDataSourceORMInspection")

package cn.chahuyun.omr.entity

import cn.chahuyun.omr.game.Equipment
import jakarta.persistence.*

/**
 * 用户信息
 */
@Entity
@Table(name = "omr_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @Column(nullable = false, unique = true)
    val uid: Long,
    @Column(nullable = false)
    var name: String,
    var description: String?,
    /**
     * 称号id
     */
    @Column(name = "title_id")
    var titleId: Long?,
    /**
     * 装备栏id
     */
    @Column(name = "equipment_id")
    var equipmentId: Long?,
    var atk: Long = 0,
    var def: Long = 0,
    var hp: Long = 0,
    var damage: Int = 0,
    /**
     * 爆伤
     */
    @Column(name = "crit_damage")
    var critDamage: Int = 100
)

/**
 * 用户装备信息
 */
@Entity
@Table(name = "omr_user_equipment")
data class UserEquipment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    var head: String?,
    var chest: String?,
    var hands: String?,
    var legs: String?,
    var feet: String?,
    var weapons: String?,
    var necklace: String?,
    @Column(name = "left_ring")
    var leftRing: String?,
    @Column(name = "right_ring")
    var rightRing: String?,
)

/**
 * 称号
 */
@Entity
@Table(name = "omr_title")
class TitleData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @Column(nullable = false)
    val name: String,
)

/**
 * 用户多余装备信息
 */
@Entity
@Table(name = "omr_equipment_backup")
data class EquipmentData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    var num: Int = 0,
    @Convert(converter = StringListConverter::class)
    val codes: MutableList<String>
) {
    fun addEquipment(equipment: Equipment) {
        codes.add(equipment.code)
        num = codes.size
    }

    init {
        num = codes.size
    }
}


class Dungeon() {}