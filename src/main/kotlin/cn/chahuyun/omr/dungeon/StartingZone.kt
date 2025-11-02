package cn.chahuyun.omr.dungeon

import cn.chahuyun.omr.dungeon.CodesDStartingZone.STARTING_ZONE
import cn.chahuyun.omr.equipment.CodesEQNovice
import cn.chahuyun.omr.game.BaseProperty
import cn.chahuyun.omr.game.BaseProperty.Companion.create
import cn.chahuyun.omr.game.Loot
import cn.chahuyun.omr.game.LootEmptyPropCodes
import cn.chahuyun.omr.skills.CodesSFireball

object StartingZoneRegistrar {
    init {
        val startingZone1 = StartingZone(
            STARTING_ZONE,
            name = "新手村",
            bossName = "哥布林",
            bossDescription = "一只红色的哥布林",
            bossProperty = mapOf(
                1 to create(hp = 500, atk = 5, def = 5, crit = 1, critDamage = 120),
                2 to create(hp = 750, atk = 15, def = 7, crit = 1, critDamage = 120),
                3 to create(hp = 1000, atk = 25, def = 10, crit = 3, critDamage = 130),
            ),
            skills = mapOf(
                3 to listOf(CodesSFireball.FIREBALL_SKILLS_150)
            ),
            dropProp = mapOf(
                1 to listOf(
                    Loot(
                        weight = 100, lootProp = mapOf(
                            100 to CodesEQNovice.NOVICE_HELMET
                        )
                    )
                ),
                2 to listOf(
                    Loot(
                        weight = 100, lootProp = mapOf(
                            100 to CodesEQNovice.NOVICE_HELMET
                        )
                    )
                ),
                3 to listOf(
                    Loot(
                        weight = 100, lootProp = mapOf(
                            70 to CodesEQNovice.NOVICE_HELMET,
                            19 to LootEmptyPropCodes.EMPTY_PROP,
                            11 to CodesEQNovice.NOVICE_RING
                        )
                    )
                ),
            ),
        )

        DungeonFactory.register(startingZone1)
    }
}

/**
 * 副本code列表D
 */
object CodesDStartingZone {
    /**
     * 新手村1
     */
    const val STARTING_ZONE = "D-starting-zone-1"
}


/**
 * 起始区域类，代表游戏副本中的起始区域
 *
 * @param code 区域代码，用于唯一标识该区域
 * @param name 区域名称，显示给玩家的区域名字
 * @param bossName 区域Boss名称
 * @param bossDescription 区域Boss描述信息
 * @param bossProperty 区域Boss属性映射，键为难度等级，值为对应的基础属性
 * @param skills 区域技能映射，键为难度等级，值为该等级下的技能列表
 * @param dropProp 区域掉落属性映射，键为难度等级，值为该等级下的掉落物品列表
 */
class StartingZone(
    code: String, name: String, bossName: String,
    bossDescription: String,
    override val bossProperty: Map<Int, BaseProperty>,
    override val skills: Map<Int, List<String>>,
    override val dropProp: Map<Int, List<Loot>>
) : Dungeon(
    code,
    name,
    bossName,
    bossDescription
) {

}
