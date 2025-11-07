@file:Suppress("JpaDataSourceORMInspection")

package cn.chahuyun.omr.entity.data

import cn.chahuyun.hibernateplus.HibernateFactory
import cn.chahuyun.omr.occupation.Occupation
import jakarta.persistence.*

/**
 * 用户信息
 */
@Entity
@Table(name = "omr_user")
class PlayerUser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(nullable = false, unique = true) val uid: Long,
    var level: Int = 1,
    /**
     * 经验值
     */
    var exp: Long = 0,
    @Column(nullable = false) var name: String,
    var description: String? = "",
    /**
     * 称号id
     */
    @Column(name = "title_id") var titleId: Long? = null,
    /**
     * 装备栏id
     */
    @Column(name = "equipment_id") var equipmentId: Long? = null,
    /**
     * 技能栏id
     */
    @Column(name = "skills_id") var skillsId: Long? = null,
    var atk: Long = 0,
    var def: Long = 0,
    var hp: Long = 0,
    var speed: Int = 0,
    var crit: Int = 0,
    /**
     * 爆伤
     */
    @Column(name = "crit_damage") var critDamage: Int = 120,
    /**
     * 职业
     */
    var occupation: Occupation = Occupation.entries.random()
) {

    /**
     * 用户装备栏
     */
    val PlayerUser.equipmentColumn: UserEquipment
        get() = equipmentId?.let { id ->
            HibernateFactory.selectOne(UserEquipment::class.java, id)
        } ?: run {
            HibernateFactory.merge(UserEquipment(uid = uid)).also { newEquipment ->
                this.equipmentId = newEquipment.id
                HibernateFactory.merge(this)
            }
        }

    /**
     * 用户技能栏
     */
    val PlayerUser.skillsColumn: UserSkills
        get() = skillsId?.let {
            HibernateFactory.selectOne(UserSkills::class.java, it)
        } ?: run {
            HibernateFactory.merge(UserSkills(uid = uid)).also { newSkills ->
                this.skillsId = newSkills.id
                HibernateFactory.merge(this)
            }
        }
}

/**
 * 用户装备信息
 */
@Entity
@Table(name = "omr_user_equipment")
data class UserEquipment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(nullable = false) val uid: Long?,
    var head: String? = null,
    var chest: String? = null,
    var hands: String? = null,
    var legs: String? = null,
    var feet: String? = null,
    var weapons: String? = null,
    var necklace: String? = null,
    var ring: String? = null,
)

/**
 * 用户技能栏
 */
@Entity
@Table(name = "user_skills")
data class UserSkills(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) val uid: Long?,
    /**
     * 职业技能（Class Skills）
     */
    @Column(name = "class_skill")
    var classSkill: String? = null,

    //
    /**
     * 主技能（Primary Skill）
     */
    @Column(name = "primary_skill")
    var primarySkill: String? = null,

    /**
     * 副技能（Secondary Skill）
     */
    @Column(name = "secondary_skill")
    var secondarySkill: String? = null,

    /**
     * 被动技能（Passive Skill）
     */
    @Column(name = "passive_skill")
    var passiveSkill: String? = null
)