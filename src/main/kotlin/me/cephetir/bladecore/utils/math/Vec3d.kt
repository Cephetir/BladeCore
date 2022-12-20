package me.cephetir.bladecore.utils.math

import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vec3d(xIn: Double, yIn: Double, zIn: Double) {
    var x: Double
    var y: Double
    var z: Double

    init {
        var xIn = xIn
        var yIn = yIn
        var zIn = zIn
        if (xIn == -0.0) {
            xIn = 0.0
        }
        if (yIn == -0.0) {
            yIn = 0.0
        }
        if (zIn == -0.0) {
            zIn = 0.0
        }
        x = xIn
        y = yIn
        z = zIn
    }

    fun set(xIn: Double, yIn: Double, zIn: Double) {
        x = xIn
        y = yIn
        z = zIn
    }

    fun set(vec: Vec3d) {
        x = vec.x
        y = vec.y
        z = vec.z
    }

    constructor(vector: Vec3i) : this(vector.x.toDouble(), vector.y.toDouble(), vector.z.toDouble()) {}
    constructor(vector: Vec3) : this(vector.xCoord, vector.yCoord, vector.zCoord) {}

    fun subtractReverse(vec: Vec3d): Vec3d {
        return Vec3d(vec.x - x, vec.y - y, vec.z - z)
    }

    fun normalize(): Vec3d {
        val d0 = sqrt(x * x + y * y + z * z)
        return if (d0 < 1.0E-4) ZERO else Vec3d(x / d0, y / d0, z / d0)
    }

    fun dotProduct(vec: Vec3d): Double {
        return x * vec.x + y * vec.y + z * vec.z
    }

    fun crossProduct(vec: Vec3d): Vec3d {
        return Vec3d(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x)
    }

    fun subtract(vec: Vec3d): Vec3d {
        return this.subtract(vec.x, vec.y, vec.z)
    }

    fun subtract(x: Double, y: Double, z: Double): Vec3d {
        return this.add(-x, -y, -z)
    }

    fun add(vec: Vec3d): Vec3d {
        return this.add(vec.x, vec.y, vec.z)
    }

    fun add(x: Double, y: Double, z: Double): Vec3d {
        return Vec3d(this.x + x, this.y + y, this.z + z)
    }

    fun distanceTo(vec: Vec3d): Double {
        val d0 = vec.x - x
        val d1 = vec.y - y
        val d2 = vec.z - z
        return sqrt(d0 * d0 + d1 * d1 + d2 * d2)
    }

    fun squareDistanceTo(vec: Vec3d): Double {
        val d0 = vec.x - x
        val d1 = vec.y - y
        val d2 = vec.z - z
        return d0 * d0 + d1 * d1 + d2 * d2
    }

    fun squareDistanceTo(xIn: Double, yIn: Double, zIn: Double): Double {
        val d0 = xIn - x
        val d1 = yIn - y
        val d2 = zIn - z
        return d0 * d0 + d1 * d1 + d2 * d2
    }

    fun scale(factor: Double): Vec3d {
        return Vec3d(x * factor, y * factor, z * factor)
    }

    fun length(): Double {
        return sqrt(x * x + y * y + z * z)
    }

    fun lengthSquared(): Double {
        return x * x + y * y + z * z
    }

    fun getIntermediateWithXValue(vec: Vec3d, x: Double): Vec3d? {
        val d0 = vec.x - this.x
        val d1 = vec.y - y
        val d2 = vec.z - z
        return if (d0 * d0 < 1.0000000116860974E-7) {
            null
        } else {
            val d3 = (x - this.x) / d0
            if (d3 in 0.0..1.0) Vec3d(this.x + d0 * d3, y + d1 * d3, z + d2 * d3) else null
        }
    }

    fun getIntermediateWithYValue(vec: Vec3d, y: Double): Vec3d? {
        val d0 = vec.x - x
        val d1 = vec.y - this.y
        val d2 = vec.z - z
        return if (d1 * d1 < 1.0000000116860974E-7) {
            null
        } else {
            val d3 = (y - this.y) / d1
            if (d3 in 0.0..1.0) Vec3d(x + d0 * d3, this.y + d1 * d3, z + d2 * d3) else null
        }
    }

    fun getIntermediateWithZValue(vec: Vec3d, z: Double): Vec3d? {
        val d0 = vec.x - x
        val d1 = vec.y - y
        val d2 = vec.z - this.z
        return if (d2 * d2 < 1.0000000116860974E-7) {
            null
        } else {
            val d3 = (z - this.z) / d2
            if (d3 in 0.0..1.0) Vec3d(x + d0 * d3, y + d1 * d3, this.z + d2 * d3) else null
        }
    }

    override fun equals(p_equals_1_: Any?): Boolean {
        return if (this === p_equals_1_) true
        else if (p_equals_1_ !is Vec3d) false
        else {
            if (p_equals_1_.x.compareTo(x) != 0) false
            else if (p_equals_1_.y.compareTo(y) != 0) false
            else p_equals_1_.z.compareTo(z) == 0
        }
    }

    override fun hashCode(): Int {
        var j = java.lang.Double.doubleToLongBits(x)
        var i = (j xor (j ushr 32)).toInt()
        j = java.lang.Double.doubleToLongBits(y)
        i = 31 * i + (j xor (j ushr 32)).toInt()
        j = java.lang.Double.doubleToLongBits(z)
        i = 31 * i + (j xor (j ushr 32)).toInt()
        return i
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    fun rotatePitch(pitch: Float): Vec3d {
        val f = cos(pitch)
        val f1 = sin(pitch)
        val d0 = x
        val d1 = y * f.toDouble() + z * f1.toDouble()
        val d2 = z * f.toDouble() - y * f1.toDouble()
        return Vec3d(d0, d1, d2)
    }

    fun rotateYaw(yaw: Float): Vec3d {
        val f = cos(yaw)
        val f1 = sin(yaw)
        val d0 = x * f.toDouble() + z * f1.toDouble()
        val d1 = y
        val d2 = z * f.toDouble() - x * f1.toDouble()
        return Vec3d(d0, d1, d2)
    }

    companion object {
        val ZERO = Vec3d(0.0, 0.0, 0.0)
        fun fromPitchYaw(p_189984_0_: Vec2f): Vec3d {
            return fromPitchYaw(p_189984_0_.x, p_189984_0_.y)
        }

        fun fromPitchYaw(p_189986_0_: Float, p_189986_1_: Float): Vec3d {
            val f = cos(-p_189986_1_ * 0.017453292f - Math.PI.toFloat())
            val f1 = sin(-p_189986_1_ * 0.017453292f - Math.PI.toFloat())
            val f2 = -cos(-p_189986_0_ * 0.017453292f)
            val f3 = sin(-p_189986_0_ * 0.017453292f)
            return Vec3d((f1 * f2).toDouble(), f3.toDouble(), (f * f2).toDouble())
        }
    }
}