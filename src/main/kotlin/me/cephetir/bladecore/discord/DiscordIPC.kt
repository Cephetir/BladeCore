package me.cephetir.bladecore.discord

import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.exceptions.NoDiscordClientException
import me.cephetir.bladecore.BladeCore
import me.cephetir.bladecore.core.config.BladeConfig
import me.cephetir.bladecore.mixins.accessors.MinecraftAccessor
import me.cephetir.bladecore.utils.math.MathUtils.round
import me.cephetir.bladecore.utils.mc
import me.cephetir.bladecore.utils.minecraft.InfoUtils
import me.cephetir.bladecore.utils.minecraft.TpsCalculator
import me.cephetir.bladecore.utils.threading.BackgroundScope
import java.time.OffsetDateTime

internal object DiscordIPC : AbstractDiscordIPC() {
    override val APP_ID = 870270610121564202L
    override val rpcBuilder: RichPresence.Builder = RichPresence.Builder()
        .setLargeImage("large", "BladeCore")

    override fun handleConnect() {
        BladeCore.logger.info("Starting Discord RPC...")
        try {
            ipc!!.connect()
            rpcBuilder.setStartTimestamp(OffsetDateTime.now())
            val richPresence = rpcBuilder.build()
            ipc!!.sendRichPresence(richPresence)
            BackgroundScope.launchLooping(updateJob)
            BladeCore.logger.info("Discord RPC initialised successfully!")
        } catch (e: NoDiscordClientException) {
            BladeCore.logger.error("No discord client found for RPC, stopping")
        }
    }

    override fun handleDisconnect() {
        BladeCore.logger.info("Shutting down Discord RPC...")
        BackgroundScope.cancel(updateJob)
        ipc!!.close()
    }

    override fun updateRPC(): RichPresence = rpcBuilder
        .setDetails(getLine(LineInfo.values()[BladeConfig.discordRpcDetail]))
        .setState(getLine(LineInfo.values()[BladeConfig.discordRpcState]))
        .build()

    private fun getLine(line: Any): String = when (line as LineInfo) {
        LineInfo.CLIENT -> BladeCore.MOD_NAME
        LineInfo.VERSION -> BladeCore.VERSION
        LineInfo.WORLD -> when {
            mc.isIntegratedServerRunning -> "Singleplayer"
            mc.currentServerData != null -> "Multiplayer"
            else -> "Main Menu"
        }

        LineInfo.DIMENSION -> InfoUtils.dimension()
        LineInfo.USERNAME -> mc.session.username
        LineInfo.HEALTH ->
            if (mc.thePlayer != null) "${mc.thePlayer.health.toInt()} HP"
            else "No HP"

        LineInfo.HUNGER ->
            if (mc.thePlayer != null) "${mc.thePlayer.foodStats.foodLevel} hunger"
            else "No Hunger"

        LineInfo.SERVER_IP -> InfoUtils.getServerType()
        LineInfo.SPEED -> "${"%.1f".format(InfoUtils.speed())} m/s"
        LineInfo.HELD_ITEM -> "Holding ${mc.thePlayer?.heldItem?.displayName ?: "Nothing"}"
        LineInfo.FPS -> "${MinecraftAccessor.getDebugFps()} FPS"
        LineInfo.TPS -> {
            if (mc.thePlayer != null) "${TpsCalculator.tickRate.round(2)} tps"
            else "No Tps"
        }
    }

    private enum class LineInfo {
        CLIENT, VERSION, WORLD, DIMENSION, USERNAME, HEALTH, HUNGER, SERVER_IP, SPEED, HELD_ITEM, FPS, TPS
    }
}