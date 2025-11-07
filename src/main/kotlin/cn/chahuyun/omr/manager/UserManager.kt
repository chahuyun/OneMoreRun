package cn.chahuyun.omr.manager

import cn.chahuyun.hibernateplus.HibernateFactory
import cn.chahuyun.omr.entity.data.PlayerUser
import cn.chahuyun.omr.entity.data.UserEquipment
import cn.chahuyun.omr.entity.data.UserSkills
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent

/**
 * UserManager 是一个用于管理用户相关操作的对象。
 * 该对象提供了用户管理的核心功能，包括用户信息的存储、检索和更新等操作。
 */
object UserManager {


    /**
     * 从群消息事件中获取玩家用户信息
     * @param event 群消息事件对象
     * @return PlayerUser 玩家用户对象
     */
    fun takeUser(event: GroupMessageEvent): PlayerUser = takeUser(event.sender)

    /**
     * 根据用户信息获取或创建玩家用户
     * @param user 用户对象
     * @return PlayerUser 玩家用户对象
     */
    fun takeUser(user: User): PlayerUser {
        // 尝试从数据库中查询玩家用户，如果不存在则初始化新用户
        return HibernateFactory.selectOne(PlayerUser::class.java, "uid", user.id) ?: initUser(user)
    }

    /**
     * 初始化新用户并创建相关装备和技能数据
     * @param user 用户对象
     * @return PlayerUser 初始化后的玩家用户对象
     */
    fun initUser(user: User): PlayerUser {
        val uid = user.id
        // 创建基础属性的玩家对象，并初始化装备和技能数据
        val player = PlayerUser(
            uid = uid,
            name = user.nameCardOrNick,
            atk = 20,
            def = 5,
            hp = 100,
        ).apply {
            // 创建或获取用户的装备和技能记录
            val userEquipment = HibernateFactory.merge(UserEquipment(uid = uid))
            val userSkills = HibernateFactory.merge(UserSkills(uid = uid))
            equipmentId = userEquipment.id
            skillsId = userSkills.id
        }
        // 保存并返回玩家对象
        return HibernateFactory.merge(player)
    }
}


/**
 * 获取群消息事件对应的玩家用户对象
 *
 * @return PlayerUser 玩家用户对象
 */
val GroupMessageEvent.player : PlayerUser
    get() = UserManager.takeUser(this)
