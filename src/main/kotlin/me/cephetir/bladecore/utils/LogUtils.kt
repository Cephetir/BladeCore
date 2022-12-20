package me.cephetir.bladecore.utils

import me.cephetir.bladecore.BladeCore
import me.cephetir.bladecore.utils.minecraft.ChatUtils

internal object LogUtils {
    fun info(message: String) {
        if (mc.thePlayer != null) ChatUtils.msg("§6$message")
        BladeCore.logger.info(message)
    }

    fun warn(message: String) {
        if (mc.thePlayer != null) ChatUtils.msg("§e$message")
        BladeCore.logger.warn(message)
    }

    fun error(message: String, error: Throwable? = null) {
        if (mc.thePlayer != null) ChatUtils.msg("§c$message")
        BladeCore.logger.error(message, error)
    }
}