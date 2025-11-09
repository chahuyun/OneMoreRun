package cn.chahuyun.omr.game

import cn.chahuyun.omr.GameProcessException
import cn.chahuyun.omr.dungeon.Dungeon
import cn.chahuyun.omr.entity.data.PlayerUser
import net.mamoe.mirai.contact.Group

/**
 * 游戏队伍数据类
 *
 * @property group 队伍所属的组别
 * @property players 队伍中的玩家列表，默认为空的可变列表
 * @property dungeon 当前所在的地下城，可为空
 * @property difficulty 游戏难度等级，默认为1
 * @property inBattle 是否处于战斗状态，默认为false
 */
data class GameTeam(
    val group: Group,
    val players: MutableList<PlayerUser> = mutableListOf(),
    val dungeon: Dungeon? = null,
    val difficulty:Int = 1,
    val inBattle: Boolean = false
)


fun GameTeam.toGameProcess(): GameProcess {
    if (this.players.size != 3) {
        throw GameProcessException("游戏流程异常:玩家队伍玩家数量不为3,但是游戏尝试启动!")
    }
    if (this.dungeon == null) {
        throw GameProcessException("游戏流程异常:副本未选择,但是游戏尝试启动!")
    }
    return GameProcess(group,players,dungeon,difficulty)
}