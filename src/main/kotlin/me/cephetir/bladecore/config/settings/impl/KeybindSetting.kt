package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting
import me.cephetir.bladecore.utils.mc
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.inventory.GuiEditSign
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

class KeybindSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    override var isHidden: () -> Boolean = { false },
    override var listener: (Int) -> Unit = {}

) : AbstractSetting<Int>(name, description, category, subCategory, Keyboard.KEY_NONE, isHidden, listener) {
    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
    }

    override fun validate() {
        value = value.coerceIn(-Mouse.getButtonCount(), 256)
    }

    /*
     *  Warning: Handle in-gui yourself!
     */
    fun isKeyDown(): Boolean {
        if (mc.currentScreen is GuiChat || mc.currentScreen is GuiEditSign) return false
        return if (value == 0) false
        else if (value < 0) Mouse.isButtonDown(-value)
        else Keyboard.isKeyDown(value)
    }
}