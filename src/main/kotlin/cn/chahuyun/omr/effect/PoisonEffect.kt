package cn.chahuyun.omr.effect

import cn.chahuyun.omr.entity.GameEntity
import cn.chahuyun.omr.game.DamageType
import cn.chahuyun.omr.game.GameProcess
import cn.chahuyun.omr.game.Impact
import kotlin.math.roundToLong

//PoisonEffect.kt

object PoisonEffectRegistrar {
    init {
        val poisonEffect = PoisonEffect(
            "effect-poison-S-20", "中毒",
            2, 120, 0.2f, "中毒了"
        )
        EffectFactory.register(poisonEffect)
    }
}

/**
 * 中毒debuff
 * 受害者
 */
class PoisonEffect(
    code: String, name: String,
    duration: Int, priority: Int = 100,
    override var value: Float,
    override val smallComposition: String
) : Effect(
    code = code,
    name = name,
    type = EffectType.DEBUFF,
    trigger = Trigger.ON_TURN_SETTLE,
    duration = duration,
    priority = priority,
    onNumericImpact = true
) {
    /**
     * 造成伤害或治疗
     */
    override fun applyImpact(entity: GameEntity, process: GameProcess) {
        val f = (entity.atk * value).roundToLong()
        val impact = Impact(f, "技能 投毒", source, listOf(entity), DamageType.PHYSICAL)
        entity.damageTaken.plus(impact)
        process.record.add("${entity.name} 中毒了...")
    }
}