package cn.chahuyun.omr.event

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.auth.OMRPerm
import cn.chahuyun.omr.dungeon.DungeonFactory
import cn.chahuyun.omr.game.GameTeam
import cn.chahuyun.omr.manager.player
import cn.chahuyun.omr.util.nextMessageWhere
import cn.chahuyun.omr.util.sendMsg
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.GroupMessageEvent
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@EventComponent
class GameEvent {
    val mutex = Mutex()

    val groupTeam = mutableMapOf<Group, GameTeam>()

    /**
     * 创建队伍函数
     *
     * @param event 群组消息事件，包含触发创建队伍操作的用户和群组信息
     */
    @MessageAuthorize(
        ["创建队伍"],
        groupPermissions = [OMRPerm.GAME]
    )
    suspend fun createTeam(event: GroupMessageEvent) {
        val group = event.group
        // 检查当前群组是否已存在队伍
        if (groupTeam.containsKey(group)) {
            event.sendMsg("当前群已经有一个队伍了!")
        }

        val player = event.player
        // 使用互斥锁确保线程安全地创建队伍
        mutex.withLock {
            val team = GameTeam(group).apply { players.add(player) }
            groupTeam[group] = team
            // 启动协程，3分钟后自动解散队伍
            OneMoreRun.launch {
                delay(3.toDuration(DurationUnit.MINUTES))
                dismissTeam(group)
            }
        }
        event.sendMsg("{${player.occupation.occ}} ${player.name} 发起了队伍创建!")
    }


    /**
     * 处理玩家加入队伍的指令逻辑。
     *
     * 当前群组必须已创建队伍，且未满员（最多3人），并且不能有相同职业类型的玩家。
     * 加入成功后，若队伍人数达到3人，则提示队长选择副本并设置副本信息。
     *
     * @param event 群消息事件对象，用于获取发送者、群组等信息
     */
    @MessageAuthorize(
        ["加入", "加入队伍"],
        groupPermissions = [OMRPerm.GAME]
    )
    suspend fun addTeam(event: GroupMessageEvent) {
        val group = event.group
        // 判断当前群是否已经创建了队伍
        if (!groupTeam.containsKey(group)) {
            return
        }
        val player = event.player
        val team = groupTeam[group]!!

        val users = team.players
        // 处理重复职业类别：如果已有相同职业类型的角色则拒绝加入
        if (users.any { it.occupation.type == player.occupation.type }) {
            return
        }

        mutex.withLock {
            // 队伍人数限制为3人
            if (users.size < 3) {
                users.add(player)
            } else {
                return
            }
        }
        event.sendMsg("{${player.occupation.occ}} ${player.name} 加入了队伍")
        val first = users.first()
        // 若队伍未满三人则不进行后续操作
        if (users.size != 3) {
            return
        }

        // 提示队长选择副本，并等待其输入副本名称与难度等级
        event.sendMsg("${first.name} 的队伍组建完成,请发送 副本名称 副本等级(默认1)")
        nextMessageWhere {
            // 等待队长在30秒内发送有效消息
            val nextEvent = nextUserForGroupMessageEvent(group.id, first.uid!!, 30) ?: run {
                event.sendMsg("队长选择副本超时!")
                dismissTeam(group)
                abort()
            }

            val content = nextEvent.message.contentToString()
            // 匹配格式：副本名 空格 数字（难度）
            if (content.matches("\\S+ \\d+".toRegex())) {
                val split = content.split(" ")
                val (dungeonName, dungeonDifficulty) = split[0] to split[1].toInt()
                val dungeon = DungeonFactory.getByName(dungeonName)
                if (dungeon == null) {
                    event.sendMsg("没有这个副本")
                    retry()
                }

                // 检查该副本是否存在指定难度
                if (dungeon.bossProperty.containsKey(dungeonDifficulty)) {
                    event.sendMsg("${dungeon.name} 没有这个这个难度!")
                    retry()
                }

                event.sendMsg("测试完成")
                abort()
            }
        }
    }

    /**
     * 解散超时的队伍
     *
     * 当队伍招募队友超时且不在战斗中时，发送解散消息并从队伍映射中移除该队伍
     *
     * @param group 需要解散队伍的群组对象
     */
    private suspend fun dismissTeam(group: Group) = groupTeam[group]?.let {
        // 检查队伍是否在战斗中，如果不在战斗中则解散队伍
        if (!it.inBattle) {
            val player = it.players.first()
            group.sendMessage("${player.name}的队伍招募队友超时,队伍解散!")
            mutex.withLock { groupTeam.remove(group) }
        }
    }

}