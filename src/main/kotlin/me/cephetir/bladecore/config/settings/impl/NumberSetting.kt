package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting

class NumberSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    override var value: Double = 0.0,
    var min: Double = 0.0,
    var max: Double = 0.0,
    var places: Int = 0,

    override var isHidden: () -> Boolean = { false },
    override var listener: (Double) -> Unit = {}

) : AbstractSetting<Double>(name, description, category, subCategory, value, isHidden, listener) {
    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
        if (value < min)
            value = min
        if (min >= max)
            throw IllegalArgumentException("Minimum cannot be lower or equal to maximum!")
    }
}