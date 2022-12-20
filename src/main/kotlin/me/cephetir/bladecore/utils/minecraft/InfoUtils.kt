package me.cephetir.bladecore.utils.minecraft

import me.cephetir.bladecore.utils.mc
import me.cephetir.bladecore.utils.minecraft.MovementUtils.realSpeed
import me.cephetir.bladecore.utils.player

object InfoUtils {

    fun getServerType() = if (mc.isIntegratedServerRunning) "Singleplayer" else mc.currentServerData?.serverIP
        ?: "Main Menu"

    fun ping() = player?.let { mc.netHandler?.getPlayerInfo(it.uniqueID)?.responseTime ?: 1 } ?: -1

    fun speed(): Double = (player?.realSpeed ?: 0.0) * TpsCalculator.tickRate

    fun dimension() = when (player?.dimension) {
        -1 -> "Nether"
        0 -> "Overworld"
        1 -> "End"
        else -> "No Dimension"
    }
}