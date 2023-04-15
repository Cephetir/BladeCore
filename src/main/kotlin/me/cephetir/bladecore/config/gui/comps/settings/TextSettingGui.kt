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
    private var cursorPosition = setting.value.length - 1
    private var hovered = false
    private var textColor = 0f

    override fun draw(x: Float, y: Float, width: Float, mouseX: Int, mouseY: Int) {
        val hx = x + width - 3 - (10 + ConfigGui.fontRenderer16.getStringWidth(setting.value)).coerceAtLeast(30)
        val hx1 = x + width - 3
        val hy = y + 6.5f
        val hy1 = y + 23.5f
        hovered = hx < mouseX && hx1 > mouseX && hy < mouseY && hy1 > mouseY
        cursorPosition = cursorPosition.coerceIn(0, setting.value.length - 1)

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

        RoundUtils.drawSmoothRoundedRect(
            x + width - 3 - (10 + ConfigGui.fontRenderer16.getStringWidth(setting.value)).coerceAtLeast(30),
            y + 6.5f,
            x + width - 3,
            y + 23.5f,
            8f,
            colorPrimary.rgb
        )

        RoundUtils.drawSmoothRoundedRect(
            x + width - 2 - (10 + ConfigGui.fontRenderer16.getStringWidth(setting.value)).coerceAtLeast(30),
            y + 7.5f,
            x + width - 4,
            y + 22.5f,
            8f,
            color2.rgb
        )

        ConfigGui.fontRenderer16.drawString(
            setting.value,
            x + width - 2 - (10 + ConfigGui.fontRenderer16.getStringWidth(setting.value)).coerceAtLeast(30) / 2.0 - ConfigGui.fontRenderer16.getStringWidth(setting.value) / 2.0,
            y + 15 - ConfigGui.fontRenderer16.getHeight() / 2.0,
            colorPrimary
        )
        if (focused) RenderUtils.drawRect(
            x + width - 2 -
                    (10 + ConfigGui.fontRenderer16.getStringWidth(setting.value)).coerceAtLeast(30) / 2f -
                    ConfigGui.fontRenderer16.getStringWidth(setting.value) / 2f +
                    ConfigGui.fontRenderer16.getStringWidth(setting.value.substring(0, cursorPosition)),
            y + 15 - ConfigGui.fontRenderer16.getHeight() / 2f,
            x + width - 2 -
                    (10 + ConfigGui.fontRenderer16.getStringWidth(setting.value)).coerceAtLeast(30) / 2f -
                    ConfigGui.fontRenderer16.getStringWidth(setting.value) / 2f +
                    ConfigGui.fontRenderer16.getStringWidth(setting.value.substring(0, cursorPosition)) + 2,
            y + 15 + ConfigGui.fontRenderer16.getHeight() / 2f,
            Color.WHITE
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        focused = if (hovered) !focused else false
        if (focused)
            cursorPosition = setting.value.length
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
        when (keyCode) {
            203 -> {
                cursorPosition = (cursorPosition - 1).coerceAtLeast(0)
                return
            }

            205 -> {
                cursorPosition = (cursorPosition + 1).coerceAtMost(setting.value.length - 1)
                return
            }

            14 -> {
                this.deleteFromCursor(-1)
                return
            }

            211 -> {
                this.deleteFromCursor(1)
                return
            }

            199 -> {
                cursorPosition = 0
                return
            }

            207 -> {
                cursorPosition = setting.value.length - 1
                return
            }
        }
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
            this.writeText(typedChar.toString())
    }

    private fun writeText(string: String) {
        if (setting.value.isEmpty()) {
            setting.value = string
            return
        }
        var string2 = ""
        val string3 = ChatAllowedCharacters.filterAllowedCharacters(string)
        string2 += setting.value.substring(0, cursorPosition)
        string2 += string3
        if (cursorPosition < setting.value.length) string2 += setting.value.substring(cursorPosition)
        cursorPosition += string3.length
        if (string2.length > setting.maxTextSize)
            string2 = string2.substring(0, setting.maxTextSize)
        setting.value = string2
    }

    private fun deleteFromCursor(i: Int) {
        if (setting.value.isEmpty()) return
        val bl = i < 0
        val j = if (bl) cursorPosition + i else cursorPosition
        val k = if (bl) cursorPosition else cursorPosition + i
        var string = ""
        if (j >= 0)
            string = setting.value.substring(0, j)
        if (k < setting.value.length)
            string += setting.value.substring(k)
        setting.value = string
        if (bl) cursorPosition += i
    }
}