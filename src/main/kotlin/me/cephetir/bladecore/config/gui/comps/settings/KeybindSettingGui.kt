package me.cephetir.bladecore.config.gui.comps.settings

import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.gui.comps.Frame
import me.cephetir.bladecore.config.settings.impl.KeybindSetting
import me.cephetir.bladecore.utils.ColorUtils
import me.cephetir.bladecore.utils.minecraft.render.RenderUtils
import me.cephetir.bladecore.utils.minecraft.render.RoundUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.awt.Color

class KeybindSettingGui(private val setting: KeybindSetting) : SettingGui {
    private val colorText = Color(191, 189, 193)
    private val colorTextLight = Color(229, 229, 229)
    private val colorBG = Color(0, 0, 0, 175)

    private var hovered = false
    private var textColor = 0f

    override fun draw(x: Float, y: Float, width: Float, mouseX: Int, mouseY: Int) {
        val keyName = if (listening) "..." else {
            if (setting.value == 0) "NONE"
            else if (setting.value < 0) "MOUSE_" + Mouse.getButtonName(-setting.value)
            else Keyboard.getKeyName(setting.value)
        }
        val hx = x + width - 7 - ConfigGui.fontRenderer16.getStringWidth(keyName).coerceAtLeast(30)
        val hx1 = x + width - 3
        val hy = y + 6.5f
        val hy1 = y + 23.5f
        hovered = hx < mouseX && hx1 > mouseX && hy < mouseY && hy1 > mouseY

        textColor = RenderUtils.animate(if (hovered || setting.isKeyDown()) 1f else 0f, textColor, 0.2f)
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
                Frame.colorPrimary
            )
        }

        RoundUtils.drawSmoothRoundedRect(
            x + width - 7 - ConfigGui.fontRenderer16.getStringWidth(keyName).coerceAtLeast(30),
            y + 6.5f,
            x + width - 3,
            y + 23.5f,
            8f,
            Frame.colorPrimary.rgb
        )

        RoundUtils.drawSmoothRoundedRect(
            x + width - 6 - ConfigGui.fontRenderer16.getStringWidth(keyName).coerceAtLeast(30),
            y + 7.5f,
            x + width - 4,
            y + 22.5f,
            8f,
            color2.rgb
        )

        ConfigGui.fontRenderer16.drawString(
            keyName,
            x + width - 5 - ConfigGui.fontRenderer16.getStringWidth(keyName).coerceAtLeast(30) / 2.0 - ConfigGui.fontRenderer16.getStringWidth(keyName) / 2.0,
            y + 15 - ConfigGui.fontRenderer16.getHeight() / 2.0,
            Frame.colorPrimary
        )
    }

    private var listening = false
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (hovered && mouseButton == 0)
            listening = true
        else if (listening) {
            setting.value = -mouseButton
            listening = false
        }
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (listening) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_BACK) setting.value = 0
            else setting.value = keyCode
            listening = false
        }
    }
}