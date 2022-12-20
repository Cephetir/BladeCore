package me.cephetir.bladecore.core.event.eventbus

import me.cephetir.bladecore.core.event.listener.Listener


/**
 * The basic Interface for an event bus
 */
interface IEventBus {
    /**
     * A map for events and their subscribed listeners
     *
     * <Event, Set<Listener>>
     */
    val subscribedListeners: MutableMap<Class<*>, MutableSet<Listener<*>>>

    /**
     * Subscribe an [objs]'s listeners to this event bus
     */
    fun subscribe(objs: Any)


    /**
     * unsubscribes an [objs]'s listeners from this event bus
     */
    fun unsubscribe(objs: Any)


    /**
     * Posts an event to this event bus, and calls
     * All the listeners of this event
     *
     * @return If event was canceled
     */
    fun post(event: Any): Boolean


    /**
     * Called when putting a new set to the map
     */
    fun newSet(): MutableSet<Listener<*>>
}