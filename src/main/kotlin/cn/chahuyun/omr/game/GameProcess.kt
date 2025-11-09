@file:Suppress("unused")

package cn.chahuyun.omr.game

import cn.chahuyun.omr.dungeon.Dungeon
import cn.chahuyun.omr.dungeon.Dungeon.Companion.toBoss
import cn.chahuyun.omr.entity.Boss
import cn.chahuyun.omr.entity.Player
import cn.chahuyun.omr.entity.data.PlayerUser
import net.mamoe.mirai.contact.Group

/**
 * 游戏过程
 *
 * @param group 游戏中的队伍信息
 * @param player 参与游戏的玩家列表
 * @param dungeon 游戏进行的地牢对象
 * @param difficulty 游戏难度等级
 */
class GameProcess(
    val group: Group,
    val player: List<PlayerUser>,
    val dungeon: Dungeon,
    val difficulty: Int,
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
     * 游戏开始
     */
    suspend fun start() {
        val player1 = player[0]
        val player2 = player[1]
        val player3 = player[2]
        group.sendMessage(
            """
            [${player1.occupation.occ}]${player1.name} 带领
                [${player2.occupation.occ}]${player2.name},
                [${player3.occupation.occ}]${player3.name},
            向 ${dungeon.name}($difficulty) 进发!
            
            冒险家,出发!
        """.trimIndent()
        )


    }

    /**
     * 加载用户->创建玩家实体
     * ->添加装备属性
     * ->添加装备效果
     * ->加载被动技能效果
     */
    fun loadUser(player: List<PlayerUser>): List<Player> {
        return emptyList()
    }

    /**
     * 加载地牢并生成对应的Boss对象
     *
     * @param dungeon 需要加载的地牢对象
     * @return 根据地牢和当前难度生成的Boss对象
     */
    fun loadDungeon(dungeon: Dungeon): Boss {
        return dungeon.toBoss(difficulty)
    }
}