package me.cephetir.bladecore.core.cache.impl

import me.cephetir.bladecore.core.cache.Cache

class MapCache<T, K>(val size: Int) : Cache<T, K> {
    private val cache: HashMap<T, K> = object : LinkedHashMap<T, K>(size, 0.75f, false) {
        override fun removeEldestEntry(eldest: Map.Entry<T, K>): Boolean = this.size > this@MapCache.size * 0.75f
    }

    override fun resetCache() {
        cache.clear()
    }

    override fun get(obj: T) = cache[obj]

    override fun cache(obj: T, value: K) {
        cache[obj] = value
    }

    override fun isCached(obj: T) = cache.containsKey(obj)

    override fun isValid(obj: T, value: K) = isCached(obj) && cache[obj] == value

    override fun invalidateObj(obj: T) {
        cache.remove(obj)
    }

    override fun invalidateValue(value: K) {
        cache.values.remove(value)
    }

    fun entries() = cache.entries
}