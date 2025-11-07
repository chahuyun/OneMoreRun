package cn.chahuyun.omr.event

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.omr.auth.OMRPerm
import cn.chahuyun.omr.dungeon.DungeonFactory
import cn.chahuyun.omr.entity.data.PlayerUser
import cn.chahuyun.omr.manager.player
import cn.chahuyun.omr.util.nextMessage
import cn.chahuyun.omr.util.sendMsg
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.GroupMessageEvent

@EventComponent
class GameEvent {
    val mutex = Mutex()

    val groupTeam = mutableMapOf<Group, MutableList<PlayerUser>>()

    @MessageAuthorize(
        ["创建队伍"],
        groupPermissions = [OMRPerm.GAME]
    )
    suspend fun createTeam(event: GroupMessageEvent) {
        val group = event.group
        if (groupTeam.containsKey(group)) {
            event.sendMsg("当前群已经有一个队伍了!")
        }

        val player = event.player
        mutex.withLock {
            groupTeam[group] = mutableListOf(player)
        }
        event.sendMsg("{${player.occupation.occ}} ${player.name} 发起了队伍创建!")
    }

    @MessageAuthorize(
        ["加入", "加入队伍"],
        groupPermissions = [OMRPerm.GAME]
    )
    suspend fun addTeam(event: GroupMessageEvent) {
        val group = event.group
        if (!groupTeam.containsKey(group)) {
            return
        }
        val player = event.player
        val users = groupTeam[group]!!

        // 处理重复职业类别
        if (users.any { it.occupation.type == player.occupation.type }) {
            return
        }

        mutex.withLock {
            if (users.size < 3) {
                users.add(player)
            } else {
                return
            }
        }
        event.sendMsg("{${player.occupation.occ}} ${player.name} 加入了队伍")
        val first = users.first()
        if (users.size != 3) {
            return
        }

        event.sendMsg("${first.name} 的队伍组建完成,请发送 副本名称 副本等级(默认1)")
        nextMessage {
            while (true) {
                val nextEvent = nextUserForGroupMessageEvent(group.id, first.uid, 30)
                if (nextEvent == null) {
                    event.sendMsg("队长选择副本超时!")
                    return@nextMessage
                }

                val content = nextEvent.message.contentToString()
                if (content.matches("\\S+ \\d+".toRegex())) {
                    val split = content.split("")
                    val (dungeonName, dungeonDifficulty) = split[0] to split[0].toInt()
                    val dungeon = DungeonFactory.getByName(dungeonName) ?: run {
                        event.sendMsg("没有这个副本")
                        return@nextMessage
                    }

                    if (dungeon.bossProperty.containsKey(dungeonDifficulty)) {
                        event.sendMsg("${dungeon.name} 没有这个这个难度!")
                        return@nextMessage
                    }

                    event.sendMsg("测试完成")
                    return@nextMessage
                }
            }
        }
    }


}