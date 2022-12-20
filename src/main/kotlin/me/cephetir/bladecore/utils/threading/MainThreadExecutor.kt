package me.cephetir.bladecore.utils.threading

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.completeWith
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.cephetir.bladecore.core.event.BladeEventBus
import me.cephetir.bladecore.core.event.events.RunGameLoopEvent
import me.cephetir.bladecore.core.event.listener.listener
import me.cephetir.bladecore.utils.mc

object MainThreadExecutor {
    private val jobs = ArrayList<MainThreadJob<*>>()
    private val mutex = Mutex()

    init {
        listener<RunGameLoopEvent.Start>(Int.MIN_VALUE) {
            runJobs()
        }

        BladeEventBus.subscribe(this)
    }

    private fun runJobs() {
        if (jobs.isEmpty()) return

        runBlocking {
            mutex.withLock {
                jobs.forEach {
                    it.run()
                }
                jobs.clear()
            }
        }
    }

    suspend fun <T> add(block: () -> T) =
        MainThreadJob(block).apply {
            if (mc.isCallingFromMinecraftThread) run()
            else mutex.withLock { jobs.add(this) }
        }.deferred

    private class MainThreadJob<T>(private val block: () -> T) {
        val deferred = CompletableDeferred<T>()

        fun run() {
            deferred.completeWith(
                runCatching { block.invoke() }
            )
        }
    }
}