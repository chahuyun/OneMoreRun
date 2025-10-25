package cn.chahuyun.omr

import net.mamoe.mirai.message.data.Message
import java.lang.RuntimeException

/**
 * 装备异常
 */
class EquipmentException(override val message: String) : RuntimeException()

/**
 * 效果异常
 */
class EffectException(override val message: String): RuntimeException()