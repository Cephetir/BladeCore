package me.cephetir.bladecore

import me.cephetir.bladecore.core.command.BladeCommand
import me.cephetir.bladecore.core.config.BladeConfig
import me.cephetir.bladecore.core.event.BladeEventBus
import me.cephetir.bladecore.core.listeners.SkyblockListener
import me.cephetir.bladecore.utils.ShutdownHook
import me.cephetir.bladecore.utils.threading.BackgroundScope
import net.minecraft.launchwrapper.Launch
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File


internal object BladeCore {
    const val MODID = "bladecore"
    const val MOD_NAME = "BladeCore"
    const val VERSION = "0.0.2-h"

    val logger: Logger = LogManager.getLogger("BladeCore")
    val mainDir = File(Launch.minecraftHome, "bladecore")
    val configFile = File(mainDir, "config.json")

    fun preInit() {
        logger.info("Starting BladeCore...")
    }

    fun init() {
        BladeConfig.load()
        BladeCommand.register()
        BladeEventBus.subscribe(SkyblockListener, true)

        ShutdownHook.register {
            BackgroundScope.stop()
            BladeConfig.save()
        }

        logger.info("BladeCore successfully initialized!")
    }
}