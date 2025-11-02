package cn.chahuyun.omr.dungeon

import cn.chahuyun.omr.DungeonException

object DungeonFactory {

    private val dungeonMap = mutableMapOf<String, Dungeon>()

    /**
     * 注册副本
     */
    fun register(dungeon: Dungeon) {
        val code = dungeon.code
        if (dungeonMap.containsKey(code)) {
            throw DungeonException("该副本 $code 已经被注册了")
        }
        dungeonMap[code] = dungeon
    }

    /**
     * 获取副本原型
     */
    fun get(code: String): Dungeon {
        return dungeonMap[code] ?: throw DungeonException("$code 未被注册")
    }


}