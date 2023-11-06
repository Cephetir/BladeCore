package me.cephetir.bladecore.core.config

import me.cephetir.bladecore.BladeCore
import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.SettingManager
import me.cephetir.bladecore.utils.UwUtils

object BladeConfig {
    private val sm = SettingManager(BladeCore.configFile, "BladeCore")

    fun load() {
        if (!BladeCore.mainDir.exists()) BladeCore.mainDir.mkdirs()
        if (!BladeCore.configFile.exists()) BladeCore.configFile.createNewFile()

        sm.loadConfig()
        ConfigGui.Companion
    }
    fun save() = sm.saveConfig()
    fun gui() = sm.openGui()

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

    val guiPostProcessing = sm.booleanSetting("Post Processing") {
        category = "GUI"
        description = "Controls gui blur and shadow"
        value = true
    }

    val primaryColorR = sm.numberSetting("Primary Color Red") {
        category = "GUI"
        subCategory = "Color"
        min = 0.0
        max = 255.0
        value = 170.0
    }

    val primaryColorG = sm.numberSetting("Primary Color Green") {
        category = "GUI"
        subCategory = "Color"
        min = 0.0
        max = 255.0
        value = 0.0
    }

    val primaryColorB = sm.numberSetting("Primary Color Blue") {
        category = "GUI"
        subCategory = "Color"
        min = 0.0
        max = 255.0
        value = 255.0
    }
}