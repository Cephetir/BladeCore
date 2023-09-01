package me.cephetir.bladecore.config.settings

import me.cephetir.bladecore.BladeCore

abstract class AbstractSetting<T>(
    open val name: String,
    open val description: String? = null,
    open val category: String,
    open val subCategory: String,
    value: T,
    open val isHidden: () -> Boolean = { false },
    open val listener: (T) -> Unit = {}
) {
    open var value: T = value
        set(value) {
            field = value
            runCatching {
                listener(value)
            }.onFailure {
                BladeCore.logger.error("Failed to run listener for $category/$subCategory/$name", it)
            }
        }

    abstract fun checkValues()
    abstract fun validate()
}