package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting

class DecoratorSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    var state: State = State.INFO,

    override var isHidden: () -> Boolean = { false },
    override var listener: (String) -> Unit = {}

) : AbstractSetting<String>(name, description, category, subCategory, "", isHidden, listener) {
    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
    }

    override fun validate() {}

    enum class State {
        INFO,
        WARNING,
        ERROR
    }
}