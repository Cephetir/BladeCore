package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting

class SelectorSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    var options: Array<String> = emptyArray(),

    override var isHidden: () -> Boolean = { false },
    override var listener: (Int) -> Unit = {}

) : AbstractSetting<Int>(name, description, category, subCategory, 0, isHidden, listener) {
    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
        if (options.isEmpty())
            throw IllegalArgumentException("Options cannot be empty!")
        if (value < 0 || value >= options.size)
            value = 0
    }

    override fun validate() {
        value = value.coerceIn(0, options.size - 1)
    }
}