package cn.chahuyun.omr.effect

import cn.chahuyun.omr.RandomUtil
import cn.chahuyun.omr.entity.GameEntity
import cn.chahuyun.omr.game.DamageType
import cn.chahuyun.omr.game.GameProcess

//DodgeEffect.kt


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
) : Effect(code, name, EffectType.BUFF, Trigger.ON_DAMAGE_CALCULATE, duration, priority) {
    /**
     * 计算时效果
     */
    override fun onTurn(entity: GameEntity, process: GameProcess) {
        entity.damageTaken.forEach {
            //真伤无法闪避
            if (it.damageType != DamageType.PURE) {
                if (RandomUtil.random(value)) {
                    //成功闪避,实际伤害为0
                    it.finalValue = 0
                    process.record.add("""${it.source.name} 对 ${it.targets.joinToString(",") { target -> target.name }} 的伤害被闪避了! """)
                }
            }
        }
    }
}