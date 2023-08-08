package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting
import me.cephetir.bladecore.core.event.BladeEventBus
import me.cephetir.bladecore.core.event.listener.listener
import me.cephetir.bladecore.utils.mc
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.inventory.GuiEditSign
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

class KeybindSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    override var isHidden: () -> Boolean = { false },
    override var listener: (Int) -> Unit = {},

) : AbstractSetting<Int>(name, description, category, subCategory, Keyboard.KEY_NONE, isHidden, listener) {
    var onClick: ((Boolean) -> Unit)? = null
        set(value) {
            if (value != null) BladeEventBus.subscribe(this)
            else BladeEventBus.unsubscribe(this)
            field = value
        }

    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
    }

    override fun validate() {
        value = value.coerceIn(-Mouse.getButtonCount(), 256)
    }

    fun isKeyDown(): Boolean {
        if (mc.currentScreen is GuiChat || mc.currentScreen is GuiEditSign) return false
        return if (value == 0) false
        else if (value < 0) Mouse.isButtonDown(-value)
        else Keyboard.isKeyDown(value)
    }

    private var clicked = false
    init {
        listener<TickEvent.ClientTickEvent> {
            if (mc.currentScreen == null && !clicked && isKeyDown()) {
                onClick?.invoke(true)
                clicked = true
            } else if (clicked && !isKeyDown()) {
                onClick?.invoke(false)
                clicked = false
            }
        }
    }
}