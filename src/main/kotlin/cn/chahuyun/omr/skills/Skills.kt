package cn.chahuyun.omr.skills

import cn.chahuyun.omr.game.Describable


enum class SkillsType{
    /**
     * 职业技能
     */
    CLASS,

    /**
     * 自定义技能
     */
    CUSTOM,

    /**
     * 被动技能
     */
    PASSIVE
}

abstract class Skills(
    val code: String,
    val name: String,
    val description: String,
): Describable{

}