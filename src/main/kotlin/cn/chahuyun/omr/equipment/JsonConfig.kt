package cn.chahuyun.omr.equipment

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

/**
 * JSON序列化配置管理器
 * 完全不使用 reified 的解决方案
 */
@Suppress("SameParameterValue")
object JsonConfig {
    private const val BASE_PACKAGE = "cn.chahuyun.omr.equipment"

    private var _jsonFormat: Json? = null
    private val registeredClasses = mutableSetOf<KClass<out Equipment>>()

    val jsonFormat: Json
        get() {
            if (_jsonFormat == null) {
                buildJsonFormat()
            }
            return _jsonFormat!!
        }

    /**
     * 构建JSON序列化配置 - 完全不使用 reified
     */
    private fun buildJsonFormat() {
        scanAndRegisterEquipmentClasses()

        val module = SerializersModule {
            polymorphic(Equipment::class) {
                // 手动为每个已知类注册，避免泛型问题
                registerKnownClassesManually()

                // 对于扫描到的未知类，使用安全注册
                registeredClasses.forEach { equipmentClass ->
                    if (!isManuallyRegistered(equipmentClass)) {
                        registerClassSafely(equipmentClass)
                    }
                }
            }
        }

        _jsonFormat = Json {
            prettyPrint = false
            ignoreUnknownKeys = true
            serializersModule = module
            classDiscriminator = "equipmentType"
            encodeDefaults = true
        }
    }

    /**
     * 手动注册已知类 - 这里需要你根据实际装备类填写
     */
    private fun registerKnownClassesManually() {
        // 在这里手动添加所有已知的装备类
        // 例如：
        // subclass(NoviceHelmet::class)
        // subclass(LegendarySword::class)

        // 如果没有已知类，暂时留空
    }

    /**
     * 检查类是否已手动注册
     */
    private fun isManuallyRegistered(equipmentClass: KClass<out Equipment>): Boolean {
        // 这里需要与 registerKnownClassesManually 中的类列表保持一致
        val manuallyRegisteredClasses = setOf<String>(
            // "NoviceHelmet", "LegendarySword" // 示例
        )
        return equipmentClass.simpleName in manuallyRegisteredClasses
    }

    /**
     * 安全注册类 - 完全不使用泛型推断
     */
    private fun PolymorphicModuleBuilder<Equipment>.registerClassSafely(equipmentClass: KClass<out Equipment>) {
        try {
            // 使用最安全的方式注册，不传递任何类型参数
            val subclassMethod = this::class.java.methods.find {
                it.name == "subclass" && it.parameterCount == 1 && it.parameterTypes[0] == Class::class.java
            }

            subclassMethod?.invoke(this, equipmentClass.java)
        } catch (e: Exception) {
            println("无法注册装备类 ${equipmentClass.simpleName}: ${e.message}")
        }
    }

    /**
     * 扫描并注册所有被@SerializeEquipment标记的装备类
     */
    private fun scanAndRegisterEquipmentClasses() {
        try {
            val classes = scanPackage(Thread.currentThread().contextClassLoader, BASE_PACKAGE)
            classes.forEach { clazz ->
                if (Equipment::class.java.isAssignableFrom(clazz) &&
                    !Modifier.isAbstract(clazz.modifiers)
                ) {
                    @Suppress("UNCHECKED_CAST")
                    val equipmentClass = clazz.kotlin as KClass<out Equipment>
                    registeredClasses.add(equipmentClass)
                }
            }
        } catch (e: Exception) {
            println("扫描装备类失败: ${e.message}")
        }
    }

    /**
     * 动态注册单个装备类
     */
    fun registerEquipmentClass(equipmentClass: KClass<out Equipment>) {
        if (registeredClasses.add(equipmentClass)) {
            _jsonFormat = null
        }
    }

    /**
     * 重新扫描并刷新配置
     */
    fun refresh() {
        registeredClasses.clear()
        _jsonFormat = null
        buildJsonFormat()
    }

    /**
     * 使用Reflections扫描包中的类
     */
    private fun scanPackage(loader: ClassLoader, packageName: String): Set<Class<*>> {
        return try {
            val reflections = Reflections(
                ConfigurationBuilder()
                    .forPackage(packageName, loader)
                    .addClassLoaders(loader)
                    .setScanners(Scanners.TypesAnnotated)
            )
            reflections.getTypesAnnotatedWith(SerializeEquipment::class.java)
        } catch (_: Exception) {
            emptySet()
        }
    }
}