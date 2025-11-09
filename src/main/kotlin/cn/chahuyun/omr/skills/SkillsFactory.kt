package cn.chahuyun.omr.skills

import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.SkillsException
import net.mamoe.mirai.utils.debug

object SkillsFactory {
    private val log = OneMoreRun.logger
    private val skillsMap = mutableMapOf<String, Skills>()

    /**
     * 注册技能到技能映射表中
     *
     * @param skills 要注册的技能对象，不能为空
     * @throws SkillsException 当要注册的技能代码已经存在时抛出异常
     */
    fun register(skills: Skills) {
        val code = skills.code
        // 检查技能是否已存在，避免重复注册
        if (skillsMap.containsKey(code)) {
            throw SkillsException("技能异常:该 $code 已经被注册了")
        }
        // 将技能添加到映射表中
        skillsMap[code] = skills
        log.debug { "注册技能: $code (${skills::class.simpleName})" }
    }


    /**
     * 根据技能代码获取对应的技能对象
     *
     * @param code 技能代码，用于在技能映射表中查找对应的技能对象
     * @return 根据代码找到的技能对象
     * @throws SkillsException 当指定代码的技能未注册时抛出异常
     */
    fun get(code: String): Skills {
        return skillsMap[code] ?: throw SkillsException("该code $code 技能未注册!")
    }

    /**
     * 根据代码获取对应的数据
     *
     * @param code 需要获取数据的代码
     * @return 返回与代码对应的数据
     */
    fun take(code: String) = get(code)

}