package me.cephetir.bladecore

import me.cephetir.bladecore.core.command.BladeCommand
import me.cephetir.bladecore.core.config.BladeConfig
import me.cephetir.bladecore.core.event.BladeEventBus
import me.cephetir.bladecore.core.listeners.SkyblockListener
import me.cephetir.bladecore.discord.DiscordIPC
import me.cephetir.bladecore.remote.metrics.Metrics
import me.cephetir.bladecore.utils.HttpUtils
import me.cephetir.bladecore.utils.ShutdownHook
import me.cephetir.bladecore.utils.threading.BackgroundScope
import net.minecraft.launchwrapper.Launch
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.net.URI


internal object BladeCore {
    const val MODID = "bladecore"
    const val MOD_NAME = "BladeCore"
    const val VERSION = "0.0.1"

    val logger: Logger = LogManager.getLogger("BladeCore")
    val mainDir = File(Launch.minecraftHome, "bladecore")
    val configFile = File(mainDir, "config.toml")

    val metricsUrl = URI(
        HttpUtils.sendGet(
            "https://gist.githubusercontent.com/Cephetir/71919daa9abb53bfc50889c7fa106f39/raw",
            mapOf("Content-Type" to "application/json")
        ) ?: ""
    )

    fun preInit() {
        logger.info("Starting BladeCore...")
    }

    fun init() {
        Metrics.connect()

        BladeConfig.load()
        BladeCommand.register()
        BladeEventBus.subscribe(SkyblockListener, true)

        ShutdownHook.register {
            DiscordIPC.disconnect()
            Metrics.disconnect()
            BackgroundScope.stop()
        }

        logger.info("BladeCore successfully initialized!")
    }
}