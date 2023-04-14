package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

class KeybindSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    override var value: Int = Keyboard.KEY_NONE,

    override var isHidden: () -> Boolean = { false },
    override var listener: (Int) -> Unit = {}

) : AbstractSetting<Int>(name, description, category, subCategory, value, isHidden, listener) {
    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
    }

    fun isKeyDown(): Boolean {
        return if (value == 0) false
        else if (value < 0) Mouse.isButtonDown(-value)
        else Keyboard.isKeyDown(value)
    }
}