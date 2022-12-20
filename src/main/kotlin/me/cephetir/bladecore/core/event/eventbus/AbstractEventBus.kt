package me.cephetir.bladecore.core.event.eventbus

import me.cephetir.bladecore.core.event.ICancellable
import me.cephetir.bladecore.core.event.ListenerManager
import me.cephetir.bladecore.core.event.listener.Listener

/**
 * [IEventBus] with some basic implementation
 */
abstract class AbstractEventBus : IEventBus {
    override fun subscribe(objs: Any) {
        ListenerManager.getListeners(objs)?.forEach {
            subscribedListeners.getOrPut(it.eventClass, ::newSet).add(it)
        }
    }

    override fun unsubscribe(objs: Any) {
        ListenerManager.getListeners(objs)?.forEach {
            subscribedListeners[it.eventClass]?.remove(it)
        }
    }

    override fun post(event: Any): Boolean {
        subscribedListeners[event.javaClass]?.let {
            for (listener in it) (listener as Listener<Any>).function.invoke(event)
        }
        return if (event is ICancellable) event.cancelled else false
    }
}