package me.cephetir.bladecore.config.gui.comps

import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.gui.comps.settings.*
import me.cephetir.bladecore.config.settings.AbstractSetting
import me.cephetir.bladecore.config.settings.SettingManager
import me.cephetir.bladecore.config.settings.impl.*
import java.util.*

class SubCategoryGui(private val frame: Frame, private val subCategory: SettingManager.SubCategory) {
    private val settings = LinkedHashMap<AbstractSetting<*>, SettingGui>()
    private var currentSettings = LinkedList<SettingGui>()

    init {
        for (setting in subCategory.settings) {
            when (setting) {
                is BooleanSetting -> settings[setting] = BooleanSettingGui(setting)
                is SelectorSetting -> settings[setting] = SelectorSettingGui(setting)
                is NumberSetting -> settings[setting] = NumberSettingGui(setting)
                is DecoratorSetting -> settings[setting] = DecoratorSettingGui(setting)
                is ButtonSetting -> settings[setting] = ButtonSettingGui(setting)
                is KeybindSetting -> settings[setting] = KeybindSettingGui(setting)
                is TextSetting -> settings[setting] = TextSettingGui(setting)
            }
        }
    }

    fun calcH(): Float = (currentSettings.size + 1) * 30f

    fun draw(mouseX: Int, mouseY: Int, h: Float, search: String): Float {
        currentSettings.clear()
        for ((setting, gui) in settings)
            if (!setting.isHidden() && (setting.name.contains(search, true) || setting.description?.contains(search, true) == true))
                currentSettings.add(gui)
        if (currentSettings.isEmpty()) return 0f

        val x = (frame.x + frame.width / 4f) * 2f
        val y = (frame.y + 20f) * 2f + h
        val width = (frame.width - frame.width / 4f) * 2f
        val height = (currentSettings.size + 1) * 30f

        if (frame.y + 20f > y + height) return height

        ConfigGui.fontRenderer32.drawString(
            subCategory.name,
            x + width / 2f - ConfigGui.fontRenderer32.getStringWidth(subCategory.name) / 2.0,
            (y + 15f - ConfigGui.fontRenderer32.getHeight() / 2f).toDouble(),
            Frame.colorPrimary
        )

        for ((i, drawable) in currentSettings.withIndex())
            drawable.draw(x, y + (i + 1) * 30f, width, mouseX, mouseY)

        return height
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        for (drawable in currentSettings)
            drawable.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        for (drawable in currentSettings)
            drawable.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
    }

    fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        for (drawable in currentSettings)
            drawable.mouseReleased(mouseX, mouseY, state)
    }

    fun keyTyped(typedChar: Char, keyCode: Int) {
        for (drawable in currentSettings)
            drawable.keyTyped(typedChar, keyCode)
    }
}