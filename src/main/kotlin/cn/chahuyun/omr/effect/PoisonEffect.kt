package cn.chahuyun.omr.effect

object PoisonEffectRegistrar {
    init {

    }
}

/**
 * 中毒buff
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
)