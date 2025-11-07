package cn.chahuyun.omr.util

import cn.chahuyun.authorize.utils.MessageUtilTemplate
import cn.chahuyun.omr.OneMoreRun
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.MessageEvent

/**
 * OMRMessageUtil 是一个消息工具对象，继承自 MessageUtilTemplate。
 * 该对象用于处理 OneMoreRun 模块中的消息事件。
 */
object OMRMessageUtil : MessageUtilTemplate() {

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
    OMRMessageUtil.block()
}
