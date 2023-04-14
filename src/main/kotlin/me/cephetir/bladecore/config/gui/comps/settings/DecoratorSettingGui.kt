package me.cephetir.bladecore.config.gui.comps.settings

import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.impl.DecoratorSetting
import java.awt.Color

class DecoratorSettingGui(private val setting: DecoratorSetting) : SettingGui {
    private val colorInfo = Color(200, 200, 200)
    private val colorWarning = Color(243, 207, 63)
    private val colorError = Color(232, 30, 7)

    override fun draw(x: Float, y: Float, width: Float, mouseX: Int, mouseY: Int) {
        ConfigGui.fontRenderer24.drawString(
            setting.value,
            x + 7.0,
            y + 15 - ConfigGui.fontRenderer24.getHeight() / 2.0,
            when (setting.state) {
                DecoratorSetting.State.INFO -> colorInfo
                DecoratorSetting.State.WARNING -> colorWarning
                DecoratorSetting.State.ERROR -> colorError
            }
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
    }
}