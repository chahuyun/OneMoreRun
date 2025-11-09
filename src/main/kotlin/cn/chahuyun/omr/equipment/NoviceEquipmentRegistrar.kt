@file:Suppress("unused")

package cn.chahuyun.omr.equipment
//NoviceEquipment.kt


import cn.chahuyun.omr.effect.Effect
import cn.chahuyun.omr.equipment.CodesEQNovice.NOVICE_HELMET
import cn.chahuyun.omr.equipment.CodesEQNovice.NOVICE_RING
import cn.chahuyun.omr.game.Property
import cn.chahuyun.omr.game.PropertyType

/**
 * 新手装备管理
 */
object NoviceEquipmentRegistrar {
    //初始化加载
    init {
        val noviceEquipment = NoviceHelmet()
        EquipmentFactory.register(noviceEquipment)

        val noviceRing = NoviceRing()
        EquipmentFactory.register(noviceRing)
    }
}

/**
 * 装备Code列表EQ
 */
object CodesEQNovice {
    /**
     * 新手头盔
     */
    const val NOVICE_HELMET = "E-novice-helmet"

    /**
     * 新手戒指
     */
    const val NOVICE_RING = "E-novice-ring"
}

/**
 * 新手套装
 */
class NoviceSuit() : Suit(
    "新手套装", "[新手]",
    5, mapOf(),
    mapOf(
        5 to listOf(
            Property(5, PropertyType.ATK),
            Property(5, PropertyType.DEF),
        )
    )
) {
    /**
     * 小作文描述
     */
    override val smallComposition: String = """
        新手套装
        
        保护你能完整的出生
        
        套装装备:
        头盔
        胸甲
        手套
        护腿
        鞋子
        
        套装效果:
        5 -> ATK+5 DEF+5
    """.trimIndent()
}

/**
 * 新手头盔
 */
@SerializeEquipment
@EquipmentDiscriminator(NOVICE_HELMET)
class NoviceHelmet() : Equipment(
    //装备code,需要唯一
    code = NOVICE_HELMET,
    //装备名称,如果有套装,在调用displayName返回的时候会加上套装的前缀
    name = "头盔",
    //简洁描述
    description = "村好盔",
    //类型
    type = EquipmentType.HEAD,
    //所属套装
    suit = NoviceSuit(),
    //是否是特殊装备,当这个属性为true的时候,在读取装备的时候会尝试获取装备的效果,默认为false
    special = false,
) {

    override val generateEffects: () -> List<Effect> = { emptyList() }
    override val generateProperties: () -> List<Property> = { listOf(Property(1, PropertyType.DEF)) }

    /**
     * 小作文描述
     */
    override val smallComposition: String
        get() = """
            $displayName
            品质:普通
            你出生他就戴在你的头上,你也不知道怎么来的!
            
            DEF+1
            
            --- 来自 moyuyanli 的邪恶设计
        """.trimIndent()
}

class NoviceRing() : Equipment(
    NOVICE_RING, "新手戒指",
    "一个无光泽的铁戒指",
    EquipmentType.RING,
    random = true
) {

    override val generateEffects: () -> List<Effect> = {
        emptyList()
    }

    /**
     * 装备的属性
     * 随机属性的实现方式
     * 需要 random = true 的属性支持
     */
    override val generateProperties: () -> List<Property> = {
        Property.random(3, 5, 1, PropertyType.ATK, PropertyType.DEF)
        //或者写法
        //listOf(Property.random(3, 5, PropertyType.ATK, PropertyType.DEF))
    }

    /**
     * 小作文描述
     */
    override val smallComposition: String = """
        $displayName
        品质:优秀
        
        不可多得的好东西
        
        ATk or DEF + 3~5
        
        --- 来自 moyuyanli 的祝福
    """.trimIndent()

}