package me.cephetir.bladecore.core.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import me.cephetir.bladecore.BladeCore
import me.cephetir.bladecore.discord.DiscordIPC
import me.cephetir.bladecore.utils.UwUtils
import kotlin.reflect.jvm.javaField

object BladeConfig : Vigilant(BladeCore.configFile, "BladeCore") {
    fun load() {
        if (!BladeCore.mainDir.exists()) BladeCore.mainDir.mkdirs()
        if (!BladeCore.configFile.exists()) BladeCore.configFile.createNewFile()

        registerListener<Boolean>(::discordRpc.javaField!!) {
            if (it) DiscordIPC.connect()
            else DiscordIPC.disconnect()
        }
        registerListener<Boolean>(::uwuify.javaField!!) { UwUtils.textCache.resetCache() }

        addDependency(::discordRpcDetail.javaField!!, ::discordRpc.javaField!!)
        addDependency(::discordRpcState.javaField!!, ::discordRpc.javaField!!)

        initialize()
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "Discord RPC",
        description = "Show what you're playing in discord status.",
        category = "Misc",
        subcategory = "DiscordRPC"
    )
    var discordRpc = false

    @Property(
        type = PropertyType.SELECTOR,
        name = "Discord RPC First Line",
        description = "Text to show in first line of discord RPC.",
        category = "Misc",
        subcategory = "DiscordRPC",
        options = ["CLIENT", "VERSION", "WORLD", "DIMENSION", "USERNAME", "HEALTH", "HUNGER", "SERVER IP", "SPEED", "HELD ITEM", "FPS", "TPS"]
    )
    var discordRpcDetail = 0

    @Property(
        type = PropertyType.SELECTOR,
        name = "Discord RPC Second Line",
        description = "Text to show in second line of discord RPC.",
        category = "Misc",
        subcategory = "DiscordRPC",
        options = ["CLIENT", "VERSION", "WORLD", "DIMENSION", "USERNAME", "HEALTH", "HUNGER", "SERVER IP", "SPEED", "HELD ITEM", "FPS", "TPS"]
    )
    var discordRpcState = 1

    @Property(
        type = PropertyType.SWITCH,
        name = "UwUify",
        description = "UwUify all display text on screen.",
        category = "UwU"
    )
    var uwuify = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Fix lag in multiplayer server list",
        description = "Fixes freeze when openin multiplayer server list (effective if you have too much servers saved).",
        category = "Optimization"
    )
    var serverlistFix = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Optimize Optifine Capes",
        description = "Fixes lag on world join by delaying cape download.",
        category = "Optimization"
    )
    var optifineCapes = false
}