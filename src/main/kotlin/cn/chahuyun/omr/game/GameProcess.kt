package cn.chahuyun.omr.game

import cn.chahuyun.omr.entity.Boss
import cn.chahuyun.omr.entity.Player

class GameProcess(
    /**
     * 三位玩家实体
     */
    val players:List<Player>,
    /**
     * boss实体
     */
    val boss:Boss,
    /**
     * 当前回合
     */
    var currentRound: Int = 1,
    /**
     * 记录消息
     */
    val record:MutableList<String>
){
    constructor()=this()
}