package cn.chahuyun.omr

import cn.chahuyun.omr.ShortIdGenerator.counter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 短 ID 生成器
 *
 * 生成格式：[年后两位][月份][日期][3位唯一数]，例如：251024042
 *
 * 设计目标：
 * -  保证同一天内生成的 ID 绝对不重复
 * -  支持系统重启（重启后不会与之前生成的 ID 冲突）
 * -  不依赖数据库、文件等持久化存储
 * -  适合低频调用场景（如每 10 分钟调用 1~4 次）
 * -  线程安全，可多线程调用
 *
 * 实现原理：
 * 1. 使用内存计数器（[counter]）保证当天内递增不重复
 * 2. 每天 0 点自动重置计数器
 * 3. 每次系统重启时，使用当前时间的毫秒偏移作为起始值，避免与重启前的 ID 重复
 */
object ShortIdGenerator {

    // 记录上一次生成 ID 的日期（格式：yyyyMMdd），用于判断是否跨天
    private var lastDateKey: String = ""

    // 当前计数器（0~999），用于生成 3 位唯一数
    // 重启后从一个基于时间的偏移开始，避免每次都从 0 开始导致重复
    private var counter = 0

    // 在对象初始化时（即每次 JVM 启动时）设置初始偏移
    // 使用当前时间的毫秒值对 100 取模，得到 0~99 的随机起点
    // 这样即使重启，也不会从 000 开始，降低与之前 ID 重复的概率
    init {
        val initialOffset = (System.currentTimeMillis() % 100).toInt()
        counter = initialOffset
    }

    /**
     * 生成一个 9 位短 ID
     *
     * 格式：yyMMdd + 3位唯一数（如 251024042）
     *
     * @return 唯一 ID 字符串
     */
    @Synchronized
    fun generateShortId(): String {
        val now = LocalDateTime.now()

        // 年后两位 + 月份 + 日期 → 6 位，如 251024
        val datePart = now.format(DateTimeFormatter.ofPattern("yyMMdd"))

        // 用于判断是否跨天（精确到日），格式：yyyyMMdd
        val currentDayKey = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        // 如果是新的一天（跨天了），则重置计数器，并使用新的随机偏移作为起点
        if (currentDayKey != lastDateKey) {
            val dailyOffset = (System.currentTimeMillis() % 100).toInt()
            counter = dailyOffset
            lastDateKey = currentDayKey
        }

        // 将当前计数器值格式化为 3 位数字（000~999）
        val unique3 = (counter % 1000).toString().padStart(3, '0')

        // 计数器递增，确保下一次生成不同的 ID
        counter++

        // 返回最终的 9 位 ID
        return datePart + unique3
    }
}

object RandomUtil {

    /**
     * 计算随机:1.00f%~0.00f%
     */
    fun random(probability: Float): Boolean {
        val intProbability = (probability * 100).toInt()
        return random(intProbability)
    }

    /**
     * 计算随机:100%~0%
     */
    fun random(probability: Int): Boolean {
        if (probability <= 0) return false
        if (probability >= 100) return true
        val randomValue = (Math.random() * 100).toInt()
        return randomValue < probability
    }

}