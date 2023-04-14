package me.cephetir.bladecore.config.settings

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
            listener(value)
        }

    abstract fun checkValues()
}