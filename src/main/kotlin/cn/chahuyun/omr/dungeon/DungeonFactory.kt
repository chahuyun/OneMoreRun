package cn.chahuyun.omr.dungeon

import cn.chahuyun.omr.DungeonException
import cn.chahuyun.omr.OneMoreRun
import net.mamoe.mirai.utils.debug

object DungeonFactory {
    private val log = OneMoreRun.logger

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
        log.debug { "注册副本: $code (${dungeon::class.simpleName})" }
    }

    /**
     * 获取副本原型
     */
    fun get(code: String): Dungeon {
        return dungeonMap[code] ?: throw DungeonException("$code 未被注册")
    }

    /**
     * 根据地牢名称查找对应的地牢对象
     *
     * @param name 地牢名称
     * @return 找到的地牢对象，如果未找到则返回null
     */
    fun getByName(name: String): Dungeon? {
        return dungeonMap.values.find { it.name == name }
    }


}