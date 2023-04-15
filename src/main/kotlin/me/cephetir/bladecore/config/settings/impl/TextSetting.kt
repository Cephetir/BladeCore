package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting

class TextSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    var maxTextSize: Int = 32,

    override var isHidden: () -> Boolean = { false },
    override var listener: (String) -> Unit = {}

) : AbstractSetting<String>(name, description, category, subCategory, "", isHidden, listener) {
    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
    }
}