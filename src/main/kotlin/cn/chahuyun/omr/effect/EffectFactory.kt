package cn.chahuyun.omr.effect
//EffectFactory.kt

import cn.chahuyun.omr.EffectException
import cn.chahuyun.omr.OneMoreRun
import net.mamoe.mirai.utils.debug


object EffectFactory {
    private val log = OneMoreRun.logger
    private val effectMap = mutableMapOf<String, Effect>()


    fun register(effect: Effect) {
        val code = effect.code
        if (effectMap.containsKey(code)) {
            OneMoreRun.logger.warning("$code 已被注册,新注册失败!")
            return
        }
        effectMap[code] = effect
        log.debug { "注册效果: $code (${effect::class.simpleName})" }
    }

    fun get(code: String): Effect {
        return effectMap[code] ?: throw EffectException("效果 $code 未被注册!")
    }

    fun take(code: String, value: Float? = null): Effect {
        val effect = effectMap[code] ?: throw EffectException("效果 $code 未被注册!")
        return if (value == null) {
            effect
        } else {
            effect.value = value
            effect
        }
    }


}