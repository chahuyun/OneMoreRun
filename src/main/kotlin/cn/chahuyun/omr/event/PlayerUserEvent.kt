package cn.chahuyun.omr.event

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.omr.auth.OMRPerm
import cn.chahuyun.omr.game.*
import cn.chahuyun.omr.manager.player
import cn.chahuyun.omr.util.sendMsg
import net.mamoe.mirai.event.events.GroupMessageEvent

@EventComponent
class PlayerUserEvent {
    @MessageAuthorize(
        ["我的信息"],
        groupPermissions = [OMRPerm.USER]
    )
    suspend fun viewUser(event: GroupMessageEvent) {
        val player = event.player
        event.sendMsg(
            """
            --- 用户信息 ---
            用户id: ${player.uid}
            用户名称: ${player.name}
            等级: ${player.level}
            经验: ${player.exp} / ${player.upExp} (${player.upp})
            ATK: ${player.catk} (${player.atk} + ${player.eatk})
            DEF: ${player.cdef} (${player.def} + ${player.edef})
            HP: ${player.chp} (${player.hp} + ${player.ehp})
            暴击: ${player.ccrit} (${player.crit} + ${player.ecrit})
            爆伤: ${player.ccritDamage} (${player.critDamage} + ${player.ecritDamage})
            速度: ${player.cspeed} (${player.speed} + ${player.espeed})
            --- 这是个 ${player.occupation.occ} ---
        """.trimIndent()
        )
    }

}