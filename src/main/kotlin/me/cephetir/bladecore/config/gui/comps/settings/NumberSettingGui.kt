package me.cephetir.bladecore.config.gui.comps.settings

import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.impl.NumberSetting
import me.cephetir.bladecore.utils.ColorUtils
import me.cephetir.bladecore.utils.math.MathUtils.round
import me.cephetir.bladecore.utils.minecraft.render.RenderUtils
import me.cephetir.bladecore.utils.minecraft.render.RoundUtils
import java.awt.Color

class NumberSettingGui(private val setting: NumberSetting) : SettingGui {
    private val colorPrimary = Color(170, 0, 255)
    private val colorText = Color(191, 189, 193)
    private val colorTextLight = Color(229, 229, 229)
    private val colorBG = Color(0, 0, 0, 175)

    private var hovered = false
    private var hoverProgress = 0f
    private var pixelsProgess = 0f

    private var x1 = -1f
    private var x2 = -1f

    override fun draw(x: Float, y: Float, width: Float, mouseX: Int, mouseY: Int) {
        val hx = x + width / 2 + width / 5 - 1
        val hx1 = x + width - 7
        val hy = y + 13f
        val hy1 = y + 18f
        hovered = hx < mouseX && hx1 > mouseX && hy < mouseY && hy1 > mouseY
        x1 = x + width / 2 + width / 5
        x2 = x + width - 8
        val percentage = (setting.value - setting.min) / (setting.max - setting.min)
        pixelsProgess = RenderUtils.animate((x2 - x1) * percentage.toFloat(), pixelsProgess, 0.3f)

        hoverProgress = RenderUtils.animate(if (hovered || wasHovered) 1f else 0f, hoverProgress, 0.2f)
        val color2 = when {
            hoverProgress < 0.1f -> colorText
            hoverProgress > 0.9f -> colorTextLight
            else -> ColorUtils.blendColors(
                floatArrayOf(0f, 1f),
                arrayOf(colorText, colorTextLight),
                hoverProgress
            )
        }

        ConfigGui.fontRenderer24.drawString(
            setting.name,
            x + 7.0,
            y + 15 - ConfigGui.fontRenderer24.getHeight() / 2.0,
            colorText
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

        RenderUtils.drawRect(
            x + width / 2 + width / 5,
            y + 14.5f,
            x + width - 8,
            y + 15.5f,
            color2
        )

        RenderUtils.drawFilledCircle(
            x + width / 2 + width / 5 + pixelsProgess,
            y + 15f,
            3f + hoverProgress * 2f,
            colorPrimary
        )

        ConfigGui.fontRenderer16.drawString(
            setting.min.toString().removeSuffix(".0"),
            x1.toDouble(),
            y + 15 + ConfigGui.fontRenderer24.getHeight() / 2.0,
            colorText
        )
        ConfigGui.fontRenderer16.drawString(
            setting.max.toString().removeSuffix(".0"),
            x2.toDouble() - ConfigGui.fontRenderer16.getStringWidth(setting.max.toString().removeSuffix(".0")),
            y + 15 + ConfigGui.fontRenderer24.getHeight() / 2.0,
            colorText
        )
        ConfigGui.fontRenderer16.drawString(
            setting.value.toString().removeSuffix(".0"),
            (x + width / 2 + width / 5 + pixelsProgess).toDouble() - ConfigGui.fontRenderer16.getStringWidth(setting.value.toString().removeSuffix(".0")) / 2.0,
            y + 12.5 - ConfigGui.fontRenderer24.getHeight(),
            colorText
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        wasHovered = hovered
    }

    private var wasHovered = false
    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        if (wasHovered) {
            val mx = mouseX
            val pixels = mx - x1
            val percentage = pixels / (x2 - x1)
            val value = percentage * (setting.max - setting.min) + setting.min
            setting.value = value.round(setting.places).coerceIn(setting.min, setting.max)
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        wasHovered = false
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
    }
}