package me.cephetir.bladecore.core.cache

interface Cache<T, K> {
    fun resetCache()
    fun cache(obj: T, value: K)
    fun get(obj: T): K?
    fun isValid(obj: T, value: K): Boolean
    fun isCached(obj: T): Boolean
    fun invalidateObj(obj: T)
    fun invalidateValue(value: K)
}