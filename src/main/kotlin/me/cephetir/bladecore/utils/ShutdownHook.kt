package me.cephetir.bladecore.utils

import java.util.concurrent.ConcurrentLinkedQueue

object ShutdownHook {
    private val hooks = ConcurrentLinkedQueue<Runnable>()

    fun execute() {
        for (runnable in hooks) {
            try {
                runnable.run()
            } catch (ex: Exception) {
                LogUtils.error("Failed to run shutdownhook!", ex)
            }
        }
    }

    fun register(runnable: Runnable) {
        hooks.add(runnable)
    }

    fun unregister(runnable: Runnable) {
        hooks.remove(runnable)
    }
}