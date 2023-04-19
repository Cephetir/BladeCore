package me.cephetir.bladecore.config.gui.comps.settings

import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.impl.TextSetting
import me.cephetir.bladecore.utils.ColorUtils
import me.cephetir.bladecore.utils.minecraft.render.RenderUtils
import me.cephetir.bladecore.utils.minecraft.render.RoundUtils
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ChatAllowedCharacters
import java.awt.Color

class TextSettingGui(private val setting: TextSetting) : SettingGui {
    private val colorPrimary = Color(170, 0, 255)
    private val colorText = Color(191, 189, 193)
    private val colorTextLight = Color(229, 229, 229)
    private val colorBG = Color(0, 0, 0, 175)

    private var focused = false
    private var hovered = false
    private var textColor = 0f
    private var chars = -1

    override fun draw(x: Float, y: Float, width: Float, mouseX: Int, mouseY: Int) {
        val hx = x + width - 3 - (10 + ConfigGui.fontRenderer16.getStringWidth(setting.value)).coerceAtLeast(30)
        val hx1 = x + width - 3
        val hy = y + 6.5f
        val hy1 = y + 23.5f
        hovered = hx < mouseX && hx1 > mouseX && hy < mouseY && hy1 > mouseY

        textColor = RenderUtils.animate(if (hovered || focused) 1f else 0f, textColor, 0.2f)
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

        val w = (10 + ConfigGui.fontRenderer16.getStringWidth(setting.value)).coerceIn(30, 120)
        RoundUtils.drawSmoothRoundedRect(
            x + width - 3 - w,
            y + 6.5f,
            x + width - 3,
            y + 23.5f,
            8f,
            colorPrimary.rgb
        )

        RoundUtils.drawSmoothRoundedRect(
            x + width - 2 - w,
            y + 7.5f,
            x + width - 4,
            y + 22.5f,
            8f,
            color2.rgb
        )

        val scroll = w <= ConfigGui.fontRenderer16.getStringWidth(setting.value) + 4
        val text = if (!scroll) setting.value
        else {
            if (chars == -1) {
                chars = 5
                while (w <= ConfigGui.fontRenderer16.getStringWidth(setting.value.substring(chars)) + 4)
                    chars++
            }

            setting.value.substring(chars)
        }
        ConfigGui.fontRenderer16.drawString(
            text,
            x + width - 2 - w / 2.0 - ConfigGui.fontRenderer16.getStringWidth(text) / 2.0,
            y + 15 - ConfigGui.fontRenderer16.getHeight() / 2.0,
            colorPrimary
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        focused = if (hovered) !focused else false
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (!focused) return

        if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            writeText(GuiScreen.getClipboardString())
            return
        }
        if (keyCode == 14)
            return delete()
        if (keyCode == 211)
            return deleteAll()
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
            this.writeText(typedChar.toString())
    }

    private fun writeText(string: String) {
        if (setting.value.isEmpty()) {
            setting.value = string
            return
        }
        var string2 = setting.value
        val string3 = ChatAllowedCharacters.filterAllowedCharacters(string)
        string2 += string3
        setting.value = string2.substring(0, string2.length.coerceAtMost(setting.maxTextSize))
        chars = -1
    }

    private var lastPress = -1L
    private fun delete() {
        if (setting.value.isEmpty()) return
        val currTime = System.currentTimeMillis()
        if (currTime - lastPress < 175)
            return deleteAll()
        lastPress = currTime

        setting.value = setting.value.substring(0, setting.value.length - 1)
        chars = -1
    }

    private fun deleteAll() {
        setting.value = ""
        chars = -1
    }
}