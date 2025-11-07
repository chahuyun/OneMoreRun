@file:Suppress("unused")

package cn.chahuyun.omr.game

import cn.chahuyun.omr.entity.Boss
import cn.chahuyun.omr.entity.Player
import cn.chahuyun.omr.entity.data.Dungeon
import cn.chahuyun.omr.entity.data.PlayerUser

/**
 * 游戏过程
 * @param player 玩家用户list
 * @param dungeon 副本
 */
class GameProcess(
    player: List<PlayerUser>,
    dungeon: Dungeon
) {

    /**
     * 三位玩家实体
     */
    val players: List<Player> = loadUser(player)

    /**
     * boss实体
     */
    val boss: Boss = loadDungeon(dungeon)

    /**
     * 当前回合
     */
    var currentRound: Int = 1

    /**
     * 记录消息
     */
    val record: MutableList<String> = mutableListOf()

    /**
     * 加载用户->创建玩家实体
     * ->添加装备属性
     * ->添加装备效果
     * ->加载被动技能效果
     */
    fun loadUser(player: List<PlayerUser>): List<Player> {
        return emptyList()
    }

    fun loadDungeon(dungeon: Dungeon): Boss {
        return Boss("xx", "")
    }

}