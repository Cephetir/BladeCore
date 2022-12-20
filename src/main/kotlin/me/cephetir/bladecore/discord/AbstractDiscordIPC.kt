package me.cephetir.bladecore.discord

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import com.jagrosh.discordipc.exceptions.NoDiscordClientException
import me.cephetir.bladecore.utils.LogUtils
import me.cephetir.bladecore.utils.threading.BackgroundJob

abstract class AbstractDiscordIPC : IPC {
    protected abstract val APP_ID: Long
    protected var initialized = false
    var ipc: IPCClient? = null
        protected set
    protected abstract val rpcBuilder: RichPresence.Builder
    val isActive: Boolean get() = initialized && ipc?.status == PipeStatus.CONNECTED
    protected val updateJob = BackgroundJob("Discord RPC Update", 1000L) { update() }

    fun connect() {
        if (isActive) return

        if (!initialized) try {
            ipc = IPCClient(APP_ID)
            initialized = true
        } catch (e: UnsatisfiedLinkError) {
            LogUtils.error("Failed to initialise DiscordRPC due to missing native library", e)
            return
        }

        handleConnect()
    }

    private fun update() {
        if (!initialized) return
        when (ipc!!.status) {
            PipeStatus.CONNECTED -> ipc!!.sendRichPresence(updateRPC())
            PipeStatus.UNINITIALIZED -> tryConnect()
            PipeStatus.DISCONNECTED -> tryConnect()
            else -> {}
        }
    }

    private fun tryConnect() {
        try {
            ipc!!.connect()
        } catch (_: NoDiscordClientException) {
        }
    }

    fun disconnect() {
        if (!isActive) return
        handleDisconnect()
    }
}