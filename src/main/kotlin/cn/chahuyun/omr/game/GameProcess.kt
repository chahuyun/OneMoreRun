@file:Suppress("unused")

package cn.chahuyun.omr.game

import cn.chahuyun.omr.GameProcessException
import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.dungeon.Dungeon
import cn.chahuyun.omr.dungeon.Dungeon.Companion.toBoss
import cn.chahuyun.omr.effect.Trigger
import cn.chahuyun.omr.effect.byTrigger
import cn.chahuyun.omr.entity.Boss
import cn.chahuyun.omr.entity.GameEntity
import cn.chahuyun.omr.entity.Player
import cn.chahuyun.omr.entity.data.PlayerUser
import cn.chahuyun.omr.entity.data.PlayerUser.Companion.skillsColumn
import cn.chahuyun.omr.event.GameEvent
import cn.chahuyun.omr.game.Contstant.ATTACK_STRING
import cn.chahuyun.omr.occupation.OccupationType
import cn.chahuyun.omr.util.add
import cn.chahuyun.omr.util.addJoin
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.utils.debug
import kotlin.math.roundToLong

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
    private val log = OneMoreRun.logger

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
     * 转发消息记录器
     */
    val forwardingBuild = ForwardMessageBuilder(group)


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
                occ = it.occupation,
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
        forwardingBuild.add(group.bot, "你们向副本进发,遇见了boss ${boss.name} 开始战斗")
        battle()
    }

    private suspend fun battle() {
        //===== 副本开启时 =====
        // todo 读取装备特殊属性
        equipment()
        // todo 加载技能加成
        skills()
        // todo 加载环境加成
        environment()

        forwardingBuild.add(
            group.bot,
            """
            ${boss.description}
            
            hp: ${boss.currentHp}/${boss.hp}
            atk: ${boss.currentAtk}
            def: ${boss.currentDef}
        """.trimIndent()
        )

        var nextGameEntity = nextGameEntity()
        record.add("回合:$currentRound")
        while (true) {

            //===== 单体回合开始时 =====
            if (nextGameEntity is Player) {
                playerBattle(nextGameEntity)
            } else {
                bossBattle(nextGameEntity as Boss)
            }

            //===== 伤害结算时 =====
            impactSettlement(nextGameEntity.damageDealt)
            nextGameEntity.damageDealt.clear()

            if (boss.currentHp <= 0) {
                break
            }

            //===== 单体回合结算时 =====
            log.debug {
                players.toString() + boss.toString()
            }

            if (currentSerial % 4 == 1) {
                forwardingBuild.addJoin(group, *record.toTypedArray())
                record.clear()
                currentRound++
                record.add("回合:$currentRound")
            }
            nextGameEntity = nextGameEntity()
        }

        record.add("${nextGameEntity.name} 击杀了 ${boss.name} 你们成功了!")
        forwardingBuild.addJoin(group, *record.toTypedArray())
        group.sendMessage(forwardingBuild.build())
        record.clear()

        //===== 副本结束时 =====
        //todo 获得奖励
        end()
    }


    suspend fun end() {
        GameEvent.clientTeam(group)
    }

    private fun environment() {

    }

    private fun equipment() {

    }

    private fun skills() {

    }

    private fun playerBattle(player: Player) {
        if (player.skills.isEmpty()) {
            val impact = Impact(player.currentAtk, ATTACK_STRING, player, listOf(boss))
            player.damageDealt.add(impact)
        } else {
            //===== 技能释放时 =====
            TODO("技能不为空")
        }
    }

    private fun bossBattle(boss: Boss) {
        if (boss.skills.isEmpty()) {
            var find = players.find { it.occ.type == OccupationType.TANK }
            if (find == null) {
                find = players.random()
            }
            val impact = Impact(boss.currentAtk, ATTACK_STRING, boss, listOf(find))
            boss.damageDealt.add(impact)
        } else {
            //===== 技能释放时 =====
            TODO("技能不为空")
        }
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
            2 -> players[1]
            3 -> players[2]
            0 -> boss
            else -> throw GameProcessException("游戏流程错误:错误的下一个行动")
        }
        currentSerial++
        return entity
    }

}

/**
 * 伤害结算时方法
 */
private fun GameProcess.impactSettlement(impactList: List<Impact>) {
    for (impact in impactList) {
        val source = impact.source
        val effects = source.effects.byTrigger(Trigger.ON_DAMAGE_CALCULATE)
        for (effect in effects) {
            effect.onTurn(source, this)
        }
        impact.targets.forEach {
            val lng = 1 - (it.def / (100.0 + it.def))
            impact.finalValue = (impact.finalValue * lng).roundToLong()
            it.currentHp -= impact.finalValue
            record.add("${source.name} 的 ${impact.sourceTypeString} 对 ${it.name} 造成了 ${impact.finalValue} 的 ${impact.damageType.string} 伤害!")
        }
    }
}


