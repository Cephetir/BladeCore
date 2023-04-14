package me.cephetir.bladecore.core.config

import me.cephetir.bladecore.BladeCore
import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.SettingManager
import me.cephetir.bladecore.discord.DiscordIPC
import me.cephetir.bladecore.utils.UwUtils

object BladeConfig {
    private val sm = SettingManager(BladeCore.configFile, "BladeCore")

    fun load() {
        if (!BladeCore.mainDir.exists()) BladeCore.mainDir.mkdirs()
        if (!BladeCore.configFile.exists()) BladeCore.configFile.createNewFile()

        sm.loadConfig()
        ConfigGui.Companion
    }
    fun save() { sm.saveConfig() }
    fun gui() { sm.openGui() }

    val discordRpc = sm.booleanSetting("Discord RPC") {
        category = "Misc"
        subCategory = "DiscordRPC"
        listener = {
            if (it) DiscordIPC.connect()
            else DiscordIPC.disconnect()
        }
    }

    val discordRpcDetail = sm.selectorSetting("Discord RPC First Line") {
        category = "Misc"
        subCategory = "DiscordRPC"
        options = arrayOf("CLIENT", "VERSION", "WORLD", "DIMENSION", "USERNAME", "HEALTH", "HUNGER", "SERVER IP", "SPEED", "HELD ITEM", "FPS", "TPS")
        isHidden = { !discordRpc.value }
    }

    val discordRpcState = sm.selectorSetting("Discord RPC Second Line") {
        category = "Misc"
        subCategory = "DiscordRPC"
        options = arrayOf("CLIENT", "VERSION", "WORLD", "DIMENSION", "USERNAME", "HEALTH", "HUNGER", "SERVER IP", "SPEED", "HELD ITEM", "FPS", "TPS")
        isHidden = { !discordRpc.value }
    }

    val uwuify = sm.booleanSetting("UwUify") {
        category = "UwU"
    }

    val serverlistFix = sm.booleanSetting("Fix lag in multiplayer server list") {
        category = "Optimization"
    }

    val optifineCapes = sm.booleanSetting("Optimize Optifine Capes") {
        category = "Optimization"
        listener = { UwUtils.textCache.resetCache() }
    }
}