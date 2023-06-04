package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting

class BooleanSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    override var isHidden: () -> Boolean = { false },
    override var listener: (Boolean) -> Unit = {}

) : AbstractSetting<Boolean>(name, description, category, subCategory, false, isHidden, listener) {
    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
    }

    override fun validate() {}
}