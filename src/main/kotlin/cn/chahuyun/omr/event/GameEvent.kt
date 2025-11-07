package cn.chahuyun.omr.event

import cn.chahuyun.omr.entity.data.Dungeon
import cn.chahuyun.omr.entity.data.PlayerUser

interface GameEvent{

    fun onDungeonStart(user:List<PlayerUser>, dungeon: Dungeon)

    fun onTurnStart()

    //todo 对应触发时,应该还有个'副本Game'类保存playu和boss,同时创建参数调用这些方法
    //todo 效果应该还有创建伤害之类的,或者在技能释放时创建伤害.

}