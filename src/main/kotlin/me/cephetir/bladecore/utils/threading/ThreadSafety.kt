package me.cephetir.bladecore.utils.threading

import kotlinx.coroutines.CompletableDeferred
import me.cephetir.bladecore.core.event.ClientEvent
import me.cephetir.bladecore.core.event.ListenerManager
import me.cephetir.bladecore.core.event.SafeClientEvent
import me.cephetir.bladecore.core.event.listener.AsyncListener
import me.cephetir.bladecore.core.event.listener.DEFAULT_PRIORITY
import me.cephetir.bladecore.core.event.listener.Listener

inline fun <reified T : Any> Any.safeAsyncListener(receiveCanceled: Boolean = false, noinline function: suspend SafeClientEvent.(T) -> Unit) {
    this.safeAsyncListener(T::class.java, receiveCanceled, function)
}

fun <T : Any> Any.safeAsyncListener(clazz: Class<T>, receiveCanceled: Boolean = false, function: suspend SafeClientEvent.(T) -> Unit) {
    ListenerManager.register(this, AsyncListener(this, clazz, receiveCanceled) { runSafeSuspend { function(it) } })
}

inline fun <reified T : Any> Any.safeListener(priority: Int = DEFAULT_PRIORITY, receiveCanceled: Boolean = false, noinline function: SafeClientEvent.(T) -> Unit) {
    this.safeListener(priority, receiveCanceled, T::class.java, function)
}

fun <T : Any> Any.safeListener(priority: Int = DEFAULT_PRIORITY, receiveCanceled: Boolean = false, clazz: Class<T>, function: SafeClientEvent.(T) -> Unit) {
    ListenerManager.register(this, Listener(this, clazz, priority, receiveCanceled) { runSafe { function(it) } })
}

fun ClientEvent.toSafe() =
    if (world != null && player != null && playerController != null && connection != null) SafeClientEvent(world, player, playerController, connection)
    else null

inline fun runSafe(block: SafeClientEvent.() -> Unit) {
    ClientEvent().toSafe()?.let { block(it) }
}

inline fun <R> runSafeR(block: SafeClientEvent.() -> R): R? {
    return ClientEvent().toSafe()?.let { block(it) }
}

suspend fun <R> runSafeSuspend(block: suspend SafeClientEvent.() -> R): R? {
    return ClientEvent().toSafe()?.let { block(it) }
}

/**
 * Runs [block] on Minecraft main thread (Client thread)
 * The [block] will the called with a [SafeClientEvent] to ensure null safety.
 *
 * @return [CompletableDeferred] callback
 *
 * @see [onMainThread]
 */
suspend fun <T> onMainThreadSafe(block: SafeClientEvent.() -> T) =
    onMainThread { ClientEvent().toSafe()?.block() }

/**
 * Runs [block] on Minecraft main thread (Client thread)
 *
 * @return [CompletableDeferred] callback
 *
 * @see [onMainThreadSafe]
 */
suspend fun <T> onMainThread(block: () -> T) =
    MainThreadExecutor.add(block)