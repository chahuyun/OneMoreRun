package cn.chahuyun.omr.effect

object DodgeEffectRegistrar {
    init {
        val ordinaryDodgeEffect = DodgeEffect("effect-dodge-D", "普通闪避", value = 0.1f)
        EffectFactory.register(ordinaryDodgeEffect)
    }
}

/**
 * 闪避效果
 */
class DodgeEffect(
    code: String, name: String, duration: Int = -1, priority: Int = 100,
    override val smallComposition: String = "闪避!",
    override var value: Float,
) : Effect(code, name, EffectType.BUFF, Trigger.ON_DAMAGE_CALCULATE, duration, priority)