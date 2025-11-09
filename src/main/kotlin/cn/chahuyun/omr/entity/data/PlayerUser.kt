@file:Suppress("JpaDataSourceORMInspection")

package cn.chahuyun.omr.entity.data

import cn.chahuyun.hibernateplus.HibernateFactory
import cn.chahuyun.omr.equipment.Equipment
import cn.chahuyun.omr.equipment.EquipmentFactory
import cn.chahuyun.omr.occupation.Occupation
import cn.chahuyun.omr.skills.Skills
import cn.chahuyun.omr.skills.SkillsFactory
import jakarta.persistence.*

/**
 * 用户信息
 */
@Entity
@Table(name = "omr_user")
class PlayerUser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(nullable = false, unique = true) val uid: Long? = null,
    var level: Int = 1,
    /**
     * 经验值
     */
    var exp: Long = 0,
    @Column(nullable = false) var name: String? = null,
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

    companion object {
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


}

/**
 * 用户装备信息
 */
@Entity
@Table(name = "omr_user_equipment")
data class UserEquipment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(nullable = false) val uid: Long? = null,
    var head: String? = null,
    var chest: String? = null,
    var hands: String? = null,
    var legs: String? = null,
    var feet: String? = null,
    var weapons: String? = null,
    var necklace: String? = null,
    var ring: String? = null,
) {

    /**
     * 获取头部装备
     *
     * @return Equipment? 返回头部装备对象，如果不存在则返回null
     */
    fun head(): Equipment? {
        // 使用let函数安全地访问head属性，避免空指针异常
        // 通过EquipmentFactory.take方法获取装备实例
        return head?.let { EquipmentFactory.take(it) }
    }


    /**
     * 获取胸部装备
     *
     * @return 胸部装备对象，如果不存在则返回null
     */
    fun chest(): Equipment? {
        // 使用let函数安全地获取装备，避免空指针异常
        return chest?.let { EquipmentFactory.take(it) }
    }


    /**
     * 获取手部装备
     *
     * @return Equipment? 返回手部装备对象，如果不存在则返回null
     */
    fun hands(): Equipment? {
        // 使用let函数安全地获取装备，避免空指针异常
        return hands?.let { EquipmentFactory.take(it) }
    }


    /**
     * 获取腿部装备
     *
     * @return Equipment? 返回腿部装备对象，如果不存在则返回null
     */
    fun legs(): Equipment? {
        // 使用let函数安全地获取装备，避免空指针异常
        return legs?.let { EquipmentFactory.take(it) }
    }


    /**
     * 获取脚部装备
     *
     * @return Equipment? 脚部装备对象，如果不存在则返回null
     */
    fun feet(): Equipment? {
        // 使用let函数安全地访问feet属性，避免空指针异常
        // 通过EquipmentFactory创建或获取装备实例
        return feet?.let { EquipmentFactory.take(it) }
    }

    /**
     * 获取武器装备对象
     *
     * @return Equipment? 返回武器装备对象，如果weapons为空则返回null
     */
    fun weapons(): Equipment? {
        // 使用let函数安全调用EquipmentFactory.take方法创建装备对象
        return weapons?.let { EquipmentFactory.take(it) }
    }


    /**
     * 获取项链装备对象
     *
     * @return 项链装备对象，如果当前没有装备项链则返回null
     */
    fun necklace(): Equipment? {
        // 使用let函数安全地处理可空的necklace属性，避免空指针异常
        return necklace?.let { EquipmentFactory.take(it) }
    }


    /**
     * 获取戒指装备
     *
     * @return Equipment? 返回戒指装备对象，如果戒指不存在则返回null
     */
    fun ring(): Equipment? {
        // 使用let函数安全地访问ring属性，避免空指针异常
        // 如果ring不为null，则通过EquipmentFactory创建并返回装备对象
        return ring?.let { EquipmentFactory.take(it) }
    }

    /**
     * 获取所有装备列表
     *
     * @return 包含所有有效装备的列表，如果某个装备位置为空则不会包含在结果中
     */
    fun allEquipment(): List<Equipment> {
        // 收集所有装备位置的装备，过滤掉空值后返回
        return listOfNotNull(head(),chest(),hands(),legs(),feet(),weapons(),necklace(),ring())
    }

}

/**
 * 用户技能栏
 */
@Entity
@Table(name = "user_skills")
data class UserSkills(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) val uid: Long? = null,
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
){
    fun classSkill(): Skills {
        return classSkill?.let { SkillsFactory.take(classSkill) }
    }
}