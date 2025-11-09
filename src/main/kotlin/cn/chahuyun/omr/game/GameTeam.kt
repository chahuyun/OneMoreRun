package cn.chahuyun.omr.game

import cn.chahuyun.omr.GameProcessException
import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.dungeon.Dungeon
import cn.chahuyun.omr.entity.data.PlayerUser
import kotlinx.coroutines.launch
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
    var dungeon: Dungeon? = null,
    var difficulty: Int = 1,
    var inBattle: Boolean = false
)


/**
 * 将GameTeam对象转换为GameProcess对象
 *
 * @return GameProcess 游戏流程对象
 * @throws GameProcessException 当玩家队伍数量不为3或副本未选择时抛出异常
 */
fun GameTeam.toGameProcess(): GameProcess {
    // 验证玩家队伍数量是否为3人
    if (this.players.size != 3) {
        throw GameProcessException("游戏流程异常:玩家队伍玩家数量不为3,但是游戏尝试启动!")
    }
    // 验证游戏副本是否已选择
    val currentDungeon = this.dungeon ?: throw GameProcessException("游戏流程异常:副本未选择,但是游戏尝试启动!")
    // 创建并返回游戏流程对象
    return GameProcess(group, players, currentDungeon, difficulty)
}


/**
 * 启动游戏团队的游戏进程
 *
 * 此函数将当前游戏团队转换为游戏进程并启动该进程，
 * 用于开始游戏团队的实际游戏逻辑执行。
 */
fun GameTeam.start() {
    inBattle = true
    // 将游戏团队转换为游戏进程并启动
    OneMoreRun.launch {
        toGameProcess().start()
    }
}
