package cn.chahuyun.omr.skills

import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.SkillsException
import net.mamoe.mirai.utils.debug

object SkillsFactory {
    private val log = OneMoreRun.logger
    private val skillsMap = mutableMapOf<String, Skills>()

    fun register(skills: Skills) {
        val code = skills.code
        if (skillsMap.containsKey(code)) {
            throw SkillsException("技能异常:该 $code 已经被注册了")
        }
        skillsMap[code] = skills
        log.debug { "注册技能: $code (${skills::class.simpleName})" }
    }

}