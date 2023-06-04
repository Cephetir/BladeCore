package me.cephetir.bladecore.config.settings.impl

import me.cephetir.bladecore.config.settings.AbstractSetting
import me.cephetir.bladecore.utils.math.MathUtils.round

class NumberSetting(
    override val name: String,
    override var description: String? = null,

    override var category: String = "",
    override var subCategory: String = "",

    var min: Double = 0.0,
    var max: Double = 0.0,
    var places: Int = 0,

    override var isHidden: () -> Boolean = { false },
    override var listener: (Double) -> Unit = {}

) : AbstractSetting<Double>(name, description, category, subCategory, 0.0, isHidden, listener) {
    override fun checkValues() {
        if (category.isEmpty())
            throw IllegalArgumentException("Category cannot be empty!")
        if (value < min)
            value = min
        if (min >= max)
            throw IllegalArgumentException("Minimum cannot be lower or equal to maximum!")
    }

    override fun validate() {
        value = value.round(places).coerceIn(min, max)
    }
}