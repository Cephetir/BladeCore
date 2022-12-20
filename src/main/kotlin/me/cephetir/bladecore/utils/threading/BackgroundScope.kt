package me.cephetir.bladecore.utils.threading

import kotlinx.coroutines.*
import me.cephetir.bladecore.BladeCore

@OptIn(DelicateCoroutinesApi::class)
object BackgroundScope : CoroutineScope by CoroutineScope(newFixedThreadPoolContext(4, "BladeCore-Thread")) {
    val jobs = LinkedHashMap<BackgroundJob, Job?>()

    fun stop() {
        for ((_, job) in jobs)
            job?.cancel()
        jobs.clear()
    }

    fun launch(name: String, block: suspend CoroutineScope.() -> Unit): BackgroundJob =
        launch(BackgroundJob(name, -1L, block))

    fun launch(job: BackgroundJob): BackgroundJob {
        jobs[job] = startJob(job)

        return job
    }

    fun launchLooping(name: String, delay: Long, block: suspend CoroutineScope.() -> Unit): BackgroundJob =
        launchLooping(BackgroundJob(name, delay, block))

    fun launchLooping(job: BackgroundJob): BackgroundJob {
        jobs[job] = startJobLooping(job)

        return job
    }

    fun launchLoopingNoCancel(job: BackgroundJob): Job = startJobLoopingNoCancel(job)

    fun cancel(job: BackgroundJob) = jobs.remove(job)?.cancel()

    suspend fun cancelAndWait(job: BackgroundJob) = jobs.remove(job)?.cancelAndJoin()

    private fun startJob(job: BackgroundJob): Job = launch {
        try {
            job.block(this)
        } catch (e: Exception) {
            BladeCore.logger.warn("Error occurred while running background job ${job.name}", e)
        }
        jobs.remove(job)
    }

    private fun startJobLooping(job: BackgroundJob): Job = launch {
        while (jobs[job]?.isActive == true) {
            try {
                job.block(this)
            } catch (e: Exception) {
                BladeCore.logger.warn("Error occurred while running background job ${job.name}", e)
            }
            delay(job.delay)
        }
    }

    private fun startJobLoopingNoCancel(job: BackgroundJob): Job = launch {
        while (true) {
            try {
                job.block(this)
            } catch (e: Exception) {
                BladeCore.logger.warn("Error occurred while running background job ${job.name}", e)
            }
        }
    }
}