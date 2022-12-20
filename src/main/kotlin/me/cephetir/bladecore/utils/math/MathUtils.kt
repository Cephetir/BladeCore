package me.cephetir.bladecore.utils.math

import kotlin.math.pow
import kotlin.math.roundToInt

object MathUtils {
    fun Float.round(places: Int): Float {
        val scale = 10f.pow(places)
        return (this * scale).roundToInt() / scale
    }

    fun Double.round(places: Int): Double {
        val scale = 10.0.pow(places)
        return (this * scale).roundToInt() / scale
    }
}