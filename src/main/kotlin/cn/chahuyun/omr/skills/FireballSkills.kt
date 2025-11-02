package cn.chahuyun.omr.skills

import cn.chahuyun.omr.game.DamageType
import cn.chahuyun.omr.game.ImpactConfig
import cn.chahuyun.omr.skills.CodesSFireball.FIREBALL_SKILLS_150
import kotlin.math.roundToInt

object FireballSkillsRegistrar {
    init {
        val fireballSkills = FireballSkills(
            FIREBALL_SKILLS_150, "火焰术", "发射一枚火球攻击对方",
            SkillsType.CUSTOM, TargetType.BOSS, 2
        )
        SkillsFactory.register(fireballSkills)
    }
}

/**
 * 技能code列表S
 */
object CodesSFireball{
    const val FIREBALL_SKILLS_150 = "fireball-skills-150"
}

class FireballSkills(
    code: String,
    name: String,
    description: String,
    skillsType: SkillsType,
    target: TargetType,
    cooldown: Int
) :
    Skills(
        code, name, description, skillsType, target, cooldown
    ) {

    /**
     * 小作文描述
     */
    override val smallComposition: String = "Fireball! Fireball! Fireball!"

    /**
     * 效果codes
     */
    override fun getEffectCodes(): List<String> {
        return emptyList()
    }

    /**
     * 自定义伤害配置
     */
    override fun getImpactConfigs(): List<ImpactConfig> {
        return listOf(
            ImpactConfig(
                DamageType.PHYSICAL, calculateBaseValue = { self, _ ->
                    val f = self.atk * 1.5f
                    f.roundToInt()
                })
        )
    }

}
