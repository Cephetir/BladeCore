package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting

class ButtonSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    override var value: () -> Unit = {},

    override var isHidden: () -> Boolean = { false },
    override var listener: (() -> Unit) -> Unit = {}

) : AbstractSetting<() -> Unit>(name, description, category, subCategory, value, isHidden, listener) {
    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
    }
}