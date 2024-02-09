package fr.xgouchet.luxels.core.math

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Represents a vector in a 4D space coordinates.
 */
@Suppress("TooManyFunctions")
data class Vector4(
    val x: Double,
    val y: Double,
    val z: Double,
    val w: Double,
) {

    val xy: Vector2 by lazy { Vector2(x, y) }
    val xz: Vector2 by lazy { Vector2(x, z) }
    val xw: Vector2 by lazy { Vector2(x, w) }
    val yz: Vector2 by lazy { Vector2(y, z) }
    val yw: Vector2 by lazy { Vector2(y, w) }
    val zw: Vector2 by lazy { Vector2(z, w) }

    val xyz: Vector3 by lazy { Vector3(x, y, z) }
    val xyw: Vector3 by lazy { Vector3(x, y, w) }
    val xzw: Vector3 by lazy { Vector3(x, z, w) }
    val yzw: Vector3 by lazy { Vector3(y, z, w) }

    // region Operators

    operator fun plus(vector: Vector4): Vector4 {
        return Vector4(
            x + vector.x,
            y + vector.y,
            z + vector.z,
            w + vector.w,
        )
    }

    operator fun minus(vector: Vector4): Vector4 {
        return Vector4(
            x - vector.x,
            y - vector.y,
            z - vector.z,
            w - vector.w,
        )
    }

    operator fun times(scale: Double): Vector4 {
        return Vector4(
            x * scale,
            y * scale,
            z * scale,
            w * scale,
        )
    }

    operator fun times(scale: Vector4): Vector4 {
        return Vector4(
            x * scale.x,
            y * scale.y,
            z * scale.z,
            w * scale.w,
        )
    }

    operator fun div(scale: Double): Vector4 {
        return Vector4(
            x / scale,
            y / scale,
            z / scale,
            w / scale,
        )
    }

    operator fun div(scale: Vector4): Vector4 {
        return Vector4(
            x / scale.x,
            y / scale.y,
            z / scale.z,
            w / scale.w,
        )
    }

    operator fun unaryMinus(): Vector4 {
        return Vector4(-x, -y, -z, -w)
    }

    // endregion

    // region Length

    fun length(): Double {
        return sqrt(squaredLength())
    }

    fun squaredLength(): Double {
        return (x * x) + (y * y) + (z * z) + (w * w)
    }

    fun normalized(): Vector4 {
        val length = length()
        return if (length >= EPSILON) {
            Vector4(
                x / length,
                y / length,
                z / length,
                w / length,
            )
        } else {
            // TODO use NaN ?
            this
        }
    }

    // endregion

    // region Geometry

    infix fun dot(other: Vector4): Double {
        return (x * other.x) + (y * other.y) + (z * other.z) + (w * other.w)
    }

    fun floor(): Vector4 {
        return Vector4(
            floor(x),
            floor(y),
            floor(z),
            floor(w),
        )
    }

    fun ceil(): Vector4 {
        return Vector4(
            ceil(x),
            ceil(y),
            ceil(z),
            ceil(w),
        )
    }

    fun abs(): Vector4 {
        return Vector4(abs(x), abs(y), abs(z), abs(w))
    }

    /**
     * Reflect this vector against a surface with the given normal
     */
    fun reflect(
        normal: Vector4,
    ): Vector4 {
        return this - (normal * dot(normal) * 2.0)
    }

    /**
     * Refract this vector through a surface with the given normal.
     */
    fun refract(
        normal: Vector4,
        sourceIndex: Double,
        destinationIndex: Double,
        refractionFactor: Double,
    ): Vector4 {
        val r = (sourceIndex / destinationIndex) * refractionFactor
        val c = -(normal.dot(this))
        val k = r * c - sqrt(abs(1.0 - (r * r * (1.0 - c * c))))
        return (this * r) + (normal * k)
    }

    // endregion

    // region Utils

    fun components(): List<Double> {
        return listOf(x, y, z, w)
    }

    fun isNaN(): Boolean {
        return x.isNaN() || y.isNaN() || z.isNaN() || w.isNaN()
    }

    // endregion

    companion object {

        fun fromComponents(components: List<Double>): Vector4 {
            check(components.size == 4)
            return Vector4(components[0], components[1], components[2], components[3])
        }

        val NULL = Vector4(0.0, 0.0, 0.0, 0.0)
        val UNIT = Vector4(1.0, 1.0, 1.0, 1.0)
        val X_AXIS = Vector4(1.0, 0.0, 0.0, 0.0)
        val Y_AXIS = Vector4(0.0, 1.0, 0.0, 0.0)
        val Z_AXIS = Vector4(0.0, 0.0, 1.0, 0.0)
        val W_AXIS = Vector4(0.0, 0.0, 0.0, 1.0)
    }
}
