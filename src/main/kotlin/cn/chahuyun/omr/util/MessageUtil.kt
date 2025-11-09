package cn.chahuyun.omr.util

import cn.chahuyun.authorize.utils.MessageUtilTemplate
import cn.chahuyun.omr.OneMoreRun
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText


/**
 * 为转发消息构建器添加一条纯文本消息
 *
 * @param bot 机器人实例，用于发送消息
 * @param msg 要添加的纯文本消息内容
 * @return 返回当前转发消息构建器实例，支持链式调用
 */
fun ForwardMessageBuilder.add(group: Group, msg: String) = this.add(group.bot, PlainText(msg))
fun ForwardMessageBuilder.add(bot: Bot, msg: String) = this.add(bot, PlainText(msg))
fun ForwardMessageBuilder.addJoin(group: Group,vararg msg: String) = this.add(group.bot, PlainText(msg.joinToString("\n")))
fun ForwardMessageBuilder.addJoin(bot: Bot,vararg msg: String) = this.add(bot, PlainText(msg.joinToString("\n")))


/**
 * OMRMessageUtil 是一个消息工具对象，继承自 MessageUtilTemplate。
 * 该对象用于处理 OneMoreRun 模块中的消息事件。
 */
open class OMRMessageUtil : MessageUtilTemplate() {

    /**
     * 消息事件通道，用于接收和过滤消息事件。
     * 该通道基于全局事件通道创建，并限定在 OneMoreRun 作用域内，
     * 只接收 MessageEvent 类型的事件。
     *
     * @return EventChannel<MessageEvent> 返回一个只处理消息事件的事件通道
     */
    override val channel: EventChannel<MessageEvent> = GlobalEventChannel.parentScope(OneMoreRun)
        .filterIsInstance(MessageEvent::class)

}

/**
 * 执行OMRMessageUtil的下一个消息操作
 *
 * @param block 一个扩展函数，用于在OMRMessageUtil上下文中执行操作
 */
suspend inline fun nextMessage(noinline block: suspend OMRMessageUtil.() -> Unit) {
    // 执行传入的block函数
    OMRMessageUtil().block()
}


/**
 * FlowSignal 是一个密封类，继承自 Exception，用于表示数据流中的控制信号。
 * 它定义了两种信号类型：Retry（重试）和 Abort（中止），用于在数据流处理过程中
 * 传递控制指令。
 */
internal sealed class FlowSignal : Exception() {

    /**
     * Retry 信号类，表示需要重试当前操作。
     * 当数据流遇到可恢复的错误时，会发出此信号来指示应该重新尝试执行。
     */
    class Retry : FlowSignal() {
        /**
         * 读取解析方法，确保在反序列化时返回单例实例。
         * 这个方法是 Java 序列化机制的一部分，用于保证序列化和反序列化过程中
         * 对象的唯一性。
         *
         * @return Retry 类的单例实例
         */
        private fun readResolve(): Any = Retry()
    }

    /**
     * Abort 信号类，表示需要中止当前操作。
     * 当数据流遇到不可恢复的错误或需要提前终止时，会发出此信号来指示
     * 应该立即停止处理。
     */
    class Abort : FlowSignal() {
        /**
         * 读取解析方法，确保在反序列化时返回单例实例。
         * 这个方法是 Java 序列化机制的一部分，用于保证序列化和反序列化过程中
         * 对象的唯一性。
         *
         * @return Abort 类的单例实例
         */
        private fun readResolve(): Any = Abort()
    }
}


/**
 * OMR消息处理工具对象，提供流程控制相关的便捷方法
 */
object OMRMessageWhereBuild : OMRMessageUtil() {
    /**
     * 触发重试流程信号
     *
     * @param message 可选的错误信息，如果非空则会打印到控制台
     * @return 无返回值，直接抛出Retry异常
     * @throws FlowSignal.Retry 总是抛出重试信号异常
     */
    fun retry(message: String = ""): Nothing {
        // 如果提供了错误信息，则打印到控制台
        if (message.isNotEmpty()) println(message)
        // 抛出重试流程信号
        throw FlowSignal.Retry()
    }

    /**
     * 触发中止流程信号
     *
     * @param message 可选的错误信息，如果非空则会打印到控制台
     * @return 无返回值，直接抛出Abort异常
     * @throws FlowSignal.Abort 总是抛出中止信号异常
     */
    fun abort(message: String = ""): Nothing {
        // 如果提供了错误信息，则打印到控制台
        if (message.isNotEmpty()) println(message)
        // 抛出中止流程信号
        throw FlowSignal.Abort()
    }
}


/**
 * 执行一个挂起的代码块，并根据特定的流控制信号处理重试或中止逻辑 DSL
 *
 * ```kotlin
 * nextMessageWhere{
 *      retry() // ->continue
 *      abort() // ->return
 * }
 *```
 *
 * @param block 一个挂起的lambda表达式，在OMRMessageWhereBuild上下文中执行
 *              当抛出FlowSignal.Retry时会重新执行，当抛出FlowSignal.Abort时会中止执行
 */
internal suspend inline fun nextMessageWhere(noinline block: suspend OMRMessageWhereBuild.() -> Unit) {
    // 循环执行block，直到成功完成或被中止
    while (true) {
        try {
            OMRMessageWhereBuild.block()
            break // 成功执行完就退出
        } catch (e: FlowSignal) {
            // 根据捕获的流控制信号决定继续执行还是中止
            when (e) {
                is FlowSignal.Retry -> continue
                is FlowSignal.Abort -> return
            }
        }
    }
}


