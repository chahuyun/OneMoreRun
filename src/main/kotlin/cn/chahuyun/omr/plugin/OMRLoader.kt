@file:Suppress("UnusedExpression")

package cn.chahuyun.omr.plugin

import cn.chahuyun.omr.OneMoreRun
import cn.chahuyun.omr.dungeon.StartingZoneRegistrar
import cn.chahuyun.omr.effect.DodgeEffectRegistrar
import cn.chahuyun.omr.effect.PoisonEffectRegistrar
import cn.chahuyun.omr.equipment.NoviceEquipmentRegistrar
import cn.chahuyun.omr.skills.FireballSkillsRegistrar
import net.mamoe.mirai.utils.debug

object OMRLoader {
    private val log = OneMoreRun.logger

    /**
     * 数据加载器函数
     *
     * 该函数负责按特定顺序加载游戏所需的各类数据资源。
     * 加载顺序很重要，因为某些数据可能依赖于先前加载的数据。
     */
    fun loader() {
        log.debug { "开始加载数据" }
        //顺序不能变
        loaderEffect()
        log.debug { "效果加载完成.." }
        loaderEquipment()
        log.debug { "装备加载完成.." }
        loaderSkills()
        log.debug { "技能加载完成.." }
        loaderDungeon()
        log.debug { "副本加载完成.." }
    }

    /**
     * 加载地牢数据
     *
     * 该函数负责初始化和加载地牢相关的数据配置。
     * 通过调用StartingZoneRegistrar来完成具体的地牢注册逻辑。
     */
    private fun loaderDungeon() {
        StartingZoneRegistrar
    }

    /**
     * 加载新手装备配置数据
     *
     * 该函数用于加载新手装备注册器中的装备配置信息。
     * 通过调用NoviceEquipmentRegistrar来完成装备数据的初始化和注册。
     */
    private fun loaderEquipment() {
        NoviceEquipmentRegistrar
    }

    /**
     * 加载特效注册器
     *
     * 该函数用于初始化和加载游戏中的各种特效注册器。
     * 目前包括闪避效果注册器和中毒效果注册器。
     */
    private fun loaderEffect() {
        // 初始化闪避效果注册器
        DodgeEffectRegistrar
        // 初始化中毒效果注册器
        PoisonEffectRegistrar
    }

    /**
     * 加载技能配置数据
     *
     * 该函数用于初始化和加载游戏中的技能配置信息，
     * 通过调用FireballSkillsRegistrar来完成技能的注册和管理。
     */
    private fun loaderSkills() {
        FireballSkillsRegistrar
    }

}