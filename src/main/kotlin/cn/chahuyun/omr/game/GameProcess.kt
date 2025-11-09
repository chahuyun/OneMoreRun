@file:Suppress("unused")

package cn.chahuyun.omr.game

import cn.chahuyun.omr.GameProcessException
import cn.chahuyun.omr.dungeon.Dungeon
import cn.chahuyun.omr.dungeon.Dungeon.Companion.toBoss
import cn.chahuyun.omr.entity.Boss
import cn.chahuyun.omr.entity.GameEntity
import cn.chahuyun.omr.entity.Player
import cn.chahuyun.omr.entity.data.PlayerUser
import cn.chahuyun.omr.entity.data.PlayerUser.Companion.skillsColumn
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
     * 当前行动序列:
     * 1->玩家1
     * 2->玩家2
     * 3->玩家3
     * 4->BOSS
     */
    var currentSerial = 1

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
        val playerBaseList = player.map {
            Player(
                name = it.name!!,
                description = it.description ?: "",
                speed = it.cspeed,
                atk = it.catk,
                def = it.cdef,
                hp = it.chp,
                crit = it.ccrit,
                critDamage = it.ccritDamage,
            ).apply {
                it.skillsColumn
            }
        }
        return playerBaseList
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
        record.add("你们向副本进发,遇见了boss ${boss.name} 开始战斗")
        battle()
    }

    private suspend fun battle() {
        // todo 读取装备特殊属性
        equipment()
        // todo 加载技能加成
        skills()
        // todo 加载环境加成
        environment()
        var nextGameEntity = nextGameEntity()
        while (true) {
            if (nextGameEntity is Player) {
                playerBattle(nextGameEntity)
            } else {
                bossBattle(nextGameEntity as Boss)
            }

            if (boss.hp <= 0) {
                break
            }

            nextGameEntity = nextGameEntity()
        }
    }

    private fun environment() {

    }

    private fun equipment() {

    }

    private fun skills() {

    }

    private fun playerBattle(player: Player) {
        if (player.skills.isEmpty()) {

        } else {
            TODO("技能不为空")
        }
    }

    private fun bossBattle(boss: Boss) {

    }

    /**
     * 获取下一个游戏实体
     *
     * 根据当前序列号确定下一个执行动作的游戏实体，按照特定规则在玩家和Boss之间切换
     *
     * @return 下一个执行动作的游戏实体，可能是玩家或Boss
     * @throws GameProcessException 当游戏流程出现错误时抛出异常
     */
    private fun nextGameEntity(): GameEntity {
        // 根据当前序列号模4的结果确定下一个行动的实体
        // 序列号余数为1、2、3时返回第一个玩家，余数为0时返回Boss
        val entity = when (currentSerial % 4) {
            1 -> players[0]
            2 -> players[0]
            3 -> players[0]
            0 -> boss
            else -> throw GameProcessException("游戏流程错误:错误的下一个行动")
        }
        currentSerial++
        return entity
    }

}

