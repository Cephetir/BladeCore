package me.cephetir.bladecore.config.gui

import me.cephetir.bladecore.config.gui.comps.Frame
import me.cephetir.bladecore.config.settings.SettingManager
import me.cephetir.bladecore.utils.minecraft.render.font.CustomFontRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import java.awt.Font
import java.awt.GraphicsEnvironment

class ConfigGui(private val settingManager: SettingManager) : GuiScreen() {
    companion object {
        val fontRenderer16: CustomFontRenderer
        val fontRenderer24: CustomFontRenderer
        val fontRenderer32: CustomFontRenderer
        val fontRenderer48: CustomFontRenderer

        init {
            val stream = Minecraft.getMinecraft().resourceManager.getResource(ResourceLocation("fonts/Quicksand-Medium.ttf")).inputStream
            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
            val font = Font.createFont(0, stream)
            ge.registerFont(font)

            fontRenderer16 = CustomFontRenderer(font.deriveFont(0, 16f))
            fontRenderer24 = CustomFontRenderer(font.deriveFont(0, 24f))
            fontRenderer32 = CustomFontRenderer(font.deriveFont(0, 32f))
            fontRenderer48 = CustomFontRenderer(font.deriveFont(0, 48f))
        }
    }
    private val frame = Frame(this, settingManager)

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        frame.draw(mouseX, mouseY, partialTicks)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        frame.mouseClicked(mouseX, mouseY, mouseButton)
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        frame.mouseReleased(mouseX, mouseY, state)
        super.mouseReleased(mouseX, mouseY, state)
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        frame.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        frame.keyTyped(typedChar, keyCode)
        super.keyTyped(typedChar, keyCode)
    }

    override fun handleMouseInput() {
        frame.handleMouseInput()
        super.handleMouseInput()
    }

    override fun onGuiClosed() {
        settingManager.saveConfig()
    }
}