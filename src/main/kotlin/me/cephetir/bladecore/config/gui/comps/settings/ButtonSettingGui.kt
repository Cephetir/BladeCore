package me.cephetir.bladecore.config.gui.comps.settings

import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.impl.ButtonSetting
import me.cephetir.bladecore.utils.ColorUtils
import me.cephetir.bladecore.utils.minecraft.render.RenderUtils
import me.cephetir.bladecore.utils.minecraft.render.RoundUtils
import java.awt.Color

class ButtonSettingGui(private val setting: ButtonSetting) : SettingGui {
    private val colorPrimary = Color(170, 0, 255)
    private val colorText = Color(191, 189, 193)
    private val colorTextLight = Color(229, 229, 229)
    private val colorBG = Color(0, 0, 0, 175)

    private var hovered = false
    private var textColor = 0f

    override fun draw(x: Float, y: Float, width: Float, mouseX: Int, mouseY: Int) {
        val hx = x + 5
        val hx1 = x + 5 + (6 + ConfigGui.fontRenderer24.getStringWidth(setting.name)).coerceAtLeast(80)
        val hy = y + 12 - ConfigGui.fontRenderer24.getHeight() / 2f
        val hy1 = y + 18 + ConfigGui.fontRenderer24.getHeight() / 2f
        hovered = hx < mouseX && hx1 > mouseX && hy < mouseY && hy1 > mouseY

        textColor = RenderUtils.animate(if (hovered) 1f else 0f, textColor, 0.2f)
        val color2 = when {
            textColor < 0.1f -> colorText
            textColor > 0.9f -> colorTextLight
            else -> ColorUtils.blendColors(
                floatArrayOf(0f, 1f),
                arrayOf(colorText, colorTextLight),
                textColor
            )
        }

        RoundUtils.drawSmoothRoundedRect(
            x + 5,
            y + 12 - ConfigGui.fontRenderer24.getHeight() / 2f,
            x + 5 + (6 + ConfigGui.fontRenderer24.getStringWidth(setting.name)).coerceAtLeast(80),
            y + 18 + ConfigGui.fontRenderer24.getHeight() / 2f,
            8f,
            colorPrimary.rgb
        )

        RoundUtils.drawSmoothRoundedRect(
            x + 6,
            y + 13 - ConfigGui.fontRenderer24.getHeight() / 2f,
            x + 4 + (6 + ConfigGui.fontRenderer24.getStringWidth(setting.name)).coerceAtLeast(80),
            y + 17 + ConfigGui.fontRenderer24.getHeight() / 2f,
            8f,
            color2.rgb
        )

        ConfigGui.fontRenderer24.drawString(
            setting.name,
            x + 5 + (5 + ConfigGui.fontRenderer24.getStringWidth(setting.name)).coerceAtLeast(80) / 2.0 - ConfigGui.fontRenderer24.getStringWidth(setting.name) / 2.0,
            y + 15 - ConfigGui.fontRenderer24.getHeight() / 2.0,
            colorPrimary
        )

        val hov = x + 7 < mouseX && x + 7 + ConfigGui.fontRenderer24.getStringWidth(setting.name) > mouseX && y + 15f - ConfigGui.fontRenderer24.getHeight() / 2.0 < mouseY && y + 15f + ConfigGui.fontRenderer24.getHeight() / 2.0 > mouseY
        if (hov && setting.description != null) {
            RoundUtils.drawRoundedRect(
                mouseX + 5f,
                mouseY - 2f,
                mouseX + 9f + ConfigGui.fontRenderer16.getStringWidth(setting.description!!),
                mouseY + 2f + ConfigGui.fontRenderer16.getHeight(),
                8f,
                colorBG.rgb
            )
            ConfigGui.fontRenderer16.drawString(
                setting.description!!,
                mouseX + 7.0,
                mouseY.toDouble(),
                colorPrimary
            )
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (hovered)
            setting.value()
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
    }
}