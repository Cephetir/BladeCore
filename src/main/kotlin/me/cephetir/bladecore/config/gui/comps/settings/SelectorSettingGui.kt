package me.cephetir.bladecore.config.gui.comps.settings

import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.impl.SelectorSetting
import me.cephetir.bladecore.utils.ColorUtils
import me.cephetir.bladecore.utils.minecraft.render.RenderUtils
import me.cephetir.bladecore.utils.minecraft.render.RoundUtils
import java.awt.Color
import kotlin.math.max

class SelectorSettingGui(private val setting: SelectorSetting) : SettingGui {
    private val colorPrimary = Color(170, 0, 255)
    private val colorText = Color(191, 189, 193)
    private val colorTextLight = Color(229, 229, 229)
    private val colorBG = Color(0, 0, 0, 175)

    private var hovered = false
    private var textColor = 0f
    private var wid = -1

    init {
        setting.options.forEach {
            val w = ConfigGui.fontRenderer16.getStringWidth(it)
            wid = max(w, wid)
        }
        wid = wid.coerceAtLeast(50) + 5
    }

    override fun draw(x: Float, y: Float, width: Float, mouseX: Int, mouseY: Int) {
        val hx = x + width - 9 - wid
        val hx1 = x + width - 3
        val hy = y + 6.5f
        val hy1 = y + 23.5f
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

        RoundUtils.drawSmoothRoundedRect(
            x + width - 9 - wid,
            y + 6.5f,
            x + width - 3,
            y + 23.5f,
            7f,
            colorPrimary.rgb
        )

        RoundUtils.drawSmoothRoundedRect(
            x + width - 8 - wid,
            y + 7.5f,
            x + width - 4,
            y + 22.5f,
            7f,
            color2.rgb
        )

        val option = setting.options[setting.value]
        ConfigGui.fontRenderer16.drawString(
            option,
            (x + width - 6 - wid / 2f - ConfigGui.fontRenderer16.getStringWidth(option) / 2f).toDouble(),
            y + 15 - ConfigGui.fontRenderer16.getHeight() / 2.0,
            colorPrimary
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (hovered) {
            var new = setting.value + if (mouseButton == 1) -1 else 1
            if (new >= setting.options.size) new = 0
            else if (new < 0) new = setting.options.size - 1
            setting.value = new
        }
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
    }
}