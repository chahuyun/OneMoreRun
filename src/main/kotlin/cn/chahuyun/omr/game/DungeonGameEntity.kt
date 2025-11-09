package cn.chahuyun.omr.game

import cn.chahuyun.omr.entity.GameEntity

/**
 * 副本游戏实体
 */
class DungeonGameEntity(override var name: String = "副本", override var description: String = "副本环境") :
    GameEntity() {
    override val hp: Int = 0
    override val atk: Int = 0
    override val def: Int = 0
    override val crit: Int = 0
    override val critDamage: Int = 0
}