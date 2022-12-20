package me.cephetir.bladecore.core.event

import com.google.common.base.Throwables
import io.netty.util.internal.ConcurrentSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.cephetir.bladecore.core.event.eventbus.AbstractAsyncEventBus
import me.cephetir.bladecore.core.event.listener.AsyncListener
import me.cephetir.bladecore.core.event.listener.Listener
import me.cephetir.bladecore.mixins.accessors.EventBusAccessor
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.EventBus
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet

object BladeEventBus : AbstractAsyncEventBus() {
    override val subscribedListeners = ConcurrentHashMap<Class<*>, MutableSet<Listener<*>>>()
    override val subscribedListenersAsync = ConcurrentHashMap<Class<*>, MutableSet<AsyncListener<*>>>()

    fun subscribe(objs: Any, forge: Boolean) {
        if (forge) MinecraftForge.EVENT_BUS.register(objs)
        super.subscribe(objs)
    }

    fun unsubscribe(objs: Any, forge: Boolean) {
        if (forge) MinecraftForge.EVENT_BUS.unregister(objs)
        super.unsubscribe(objs)
    }

    fun postForge(event: Event, eventBus: EventBus): Boolean {
        val listeners = subscribedListeners[event.javaClass]
        val asyncListeners = subscribedListenersAsync[event.javaClass]

        runBlocking {
            asyncListeners!!.forEach {
                launch(Dispatchers.Default) {
                    it as AsyncListener<Any>

                    if (it.receiveCanceled || !event.isCancelable || !event.isCanceled) it.function.invoke(event)
                }
            }

            post(event, listeners?.filter { it.priority >= 0 }, null)

            val eventBusAccessor = eventBus as EventBusAccessor
            val forgeListeners = event.listenerList.getListeners(eventBusAccessor.busID)
            var index = 0
            try {
                while (index < forgeListeners.size) {
                    forgeListeners[index].invoke(event)
                    index++
                }
            } catch (throwable: Throwable) {
                eventBusAccessor.exceptionHandler.handleException(eventBus, event, forgeListeners, index, throwable)
                Throwables.propagate(throwable)
            }

            post(event, listeners?.filter { it.priority < 0 }, null)
        }

        return event.isCancelable && event.isCanceled
    }

    fun postForgePre(event: Event) {
        val listeners = subscribedListeners[event.javaClass]?.filter { it.priority >= 0 }
        val asyncListeners = subscribedListenersAsync[event.javaClass]?.filter { it.priority >= 0 }

        post(event, listeners, asyncListeners)
    }

    fun postForgePost(event: Event) {
        val listeners = subscribedListeners[event.javaClass]?.filter { it.priority < 0 }
        val asyncListeners = subscribedListenersAsync[event.javaClass]?.filter { it.priority < 0 }

        post(event, listeners, asyncListeners)
    }

    override fun post(event: Any): Boolean {
        val listeners = subscribedListeners[event.javaClass]
        val asyncListeners = subscribedListenersAsync[event.javaClass]

        return post(event, listeners, asyncListeners)
    }

    private fun post(event: Any, listeners: Collection<Listener<*>>?, asyncListeners: Collection<AsyncListener<*>>?): Boolean {
        if (asyncListeners.isNullOrEmpty() && !listeners.isNullOrEmpty())
            listeners.forEach {
                it as Listener<Any>

                if (event is Event) {
                    if (it.receiveCanceled || !event.isCancelable || !event.isCanceled) it.function.invoke(event)
                } else if (event is ICancellable) {
                    if (it.receiveCanceled || !event.cancelled) it.function.invoke(event)
                } else it.function.invoke(event)
            }
        else if (!asyncListeners.isNullOrEmpty()) runBlocking {
            asyncListeners.forEach {
                launch(Dispatchers.Default) {
                    it as AsyncListener<Any>

                    if (event is Event) {
                        if (it.receiveCanceled || !event.isCancelable || !event.isCanceled) it.function.invoke(event)
                    } else if (event is ICancellable) {
                        if (it.receiveCanceled || !event.cancelled) it.function.invoke(event)
                    } else it.function.invoke(event)
                }
            }

            listeners?.forEach {
                it as Listener<Any>

                if (event is Event) {
                    if (it.receiveCanceled || !event.isCancelable || !event.isCanceled) it.function.invoke(event)
                } else if (event is ICancellable) {
                    if (it.receiveCanceled || !event.cancelled) it.function.invoke(event)
                } else it.function.invoke(event)
            }
        }

        return (event is ICancellable && event.cancelled) || (event is Event && event.isCancelable && event.isCanceled)
    }

    override fun newSet() = ConcurrentSkipListSet<Listener<*>>(Comparator.reverseOrder())

    override fun newSetAsync() = ConcurrentSet<AsyncListener<*>>()
}