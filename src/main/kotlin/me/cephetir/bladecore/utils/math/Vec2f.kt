package me.cephetir.bladecore.utils.math

class Vec2f(val x: Float, val y: Float) {
    companion object {
        val ZERO = Vec2f(0.0f, 0.0f)
        val ONE = Vec2f(1.0f, 1.0f)
        val UNIT_X = Vec2f(1.0f, 0.0f)
        val NEGATIVE_UNIT_X = Vec2f(-1.0f, 0.0f)
        val UNIT_Y = Vec2f(0.0f, 1.0f)
        val NEGATIVE_UNIT_Y = Vec2f(0.0f, -1.0f)
        val MAX = Vec2f(Float.MAX_VALUE, Float.MAX_VALUE)
        val MIN = Vec2f(Float.MIN_VALUE, Float.MIN_VALUE)
    }
}