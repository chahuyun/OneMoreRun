package cn.chahuyun.omr.equipment
//EquipmentAnnotation.kt

/**
 * 可序列化装备注解
 * 标记所有需要支持序列化/克隆的装备类
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SerializeEquipment

/**
 * 装备类型鉴别器注解
 * 用于指定序列化时的类型标识字段
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EquipmentDiscriminator(val value: String)