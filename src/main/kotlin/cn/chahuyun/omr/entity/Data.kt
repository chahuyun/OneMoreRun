package cn.chahuyun.omr.entity

/**
 * 用户信息
 */
class User(
    val id: Long?,
    val uid: Long?,
    var name: String?,
    var description: String?,
    var titleId: Long?,
    var equipmentId: Long?,
) {}

/**
 * 用户装备信息
 */
data class UserEquipment(
    val id: Long?,
    var head: Long?,
    var chest: Long?,
    var hands: Long?,
    var legs: Long?,
    var feet: Long?,
    var weapons: Long?,
    var necklace: Long?,
    var ringLeft: Long?,
    var ringRight: Long?,
)

class TitleData(
    val id: Long?,
    val name: String?,
    val effectId: Long?,
)

/**
 * 效果记录
 */
data class EffectData(
    val id: Long?,
)

data class equipmentData(
    val id: Long?,
)


class Dungeon() {}