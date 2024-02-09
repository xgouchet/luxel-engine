package fr.xgouchet.luxels.core.math

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Represents a vector in a 3D space coordinates.
 */
@Suppress("TooManyFunctions")
data class Vector3(
    val x: Double,
    val y: Double,
    val z: Double,
) {

    val xy: Vector2 by lazy { Vector2(x, y) }
    val xz: Vector2 by lazy { Vector2(x, z) }
    val yz: Vector2 by lazy { Vector2(y, z) }

    // region Operators

    operator fun plus(vector: Vector3): Vector3 {
        return Vector3(
            x + vector.x,
            y + vector.y,
            z + vector.z,
        )
    }

    operator fun minus(vector: Vector3): Vector3 {
        return Vector3(
            x - vector.x,
            y - vector.y,
            z - vector.z,
        )
    }

    operator fun times(scale: Double): Vector3 {
        return Vector3(
            x * scale,
            y * scale,
            z * scale,
        )
    }

    operator fun times(scale: Vector3): Vector3 {
        return Vector3(
            x * scale.x,
            y * scale.y,
            z * scale.z,
        )
    }

    operator fun div(scale: Double): Vector3 {
        return Vector3(
            x / scale,
            y / scale,
            z / scale,
        )
    }

    operator fun div(scale: Vector3): Vector3 {
        return Vector3(
            x / scale.x,
            y / scale.y,
            z / scale.z,
        )
    }

    operator fun unaryMinus(): Vector3 {
        return Vector3(-x, -y, -z)
    }

    // endregion

    // region Length

    fun length(): Double {
        return sqrt(squaredLength())
    }

    fun squaredLength(): Double {
        return (x * x) + (y * y) + (z * z)
    }

    fun normalized(): Vector3 {
        val length = length()
        return if (length >= EPSILON) {
            Vector3(
                x / length,
                y / length,
                z / length,
            )
        } else {
            // TODO use NaN ?
            this
        }
    }

    // endregion

    // region Geometry

    infix fun dot(other: Vector3): Double {
        return (x * other.x) + (y * other.y) + (z * other.z)
    }

    infix fun cross(other: Vector3): Vector3 {
        return Vector3(
            (y * other.z) - (z * other.y),
            (z * other.x) - (x * other.z),
            (x * other.y) - (y * other.x),
        )
    }

    fun floor(): Vector3 {
        return Vector3(
            floor(x),
            floor(y),
            floor(z),
        )
    }

    fun ceil(): Vector3 {
        return Vector3(
            ceil(x),
            ceil(y),
            ceil(z),
        )
    }

    fun abs(): Vector3 {
        return Vector3(abs(x), abs(y), abs(z))
    }

    /**
     * Reflect this vector against a surface with the given normal
     */
    fun reflect(
        normal: Vector3,
    ): Vector3 {
        return this - (normal * dot(normal) * 2.0)
    }

    /**
     * Refract this vector through a surface with the given normal.
     */
    fun refract(
        normal: Vector3,
        sourceIndex: Double,
        destinationIndex: Double,
        refractionFactor: Double,
    ): Vector3 {
        val r = (sourceIndex / destinationIndex) * refractionFactor
        val c = -(normal.dot(this))
        val k = r * c - sqrt(abs(1.0 - (r * r * (1.0 - c * c))))
        return (this * r) + (normal * k)
    }

    // endregion

    // region Utils

    fun components(): List<Double> {
        return listOf(x, y, z)
    }

    fun isNaN(): Boolean {
        return x.isNaN() || y.isNaN() || z.isNaN()
    }

    // endregion

    companion object {

        fun fromComponents(components: List<Double>): Vector3 {
            check(components.size == 3)
            return Vector3(components[0], components[1], components[2])
        }

        /**
         * @param polar (between 0 and 2π)
         * @param azimuth (between -π/2 and π/2)
         * @param radius (between 0 and ∞)
         */
        fun fromSpherical(polar: Double, azimuth: Double = 0.0, radius: Double = 1.0): Vector3 {
            val cosA = cos(azimuth)
            return Vector3(
                radius * cos(polar) * cosA,
                radius * sin(polar) * cosA,
                radius * sin(azimuth),
            )
        }

        val NULL = Vector3(0.0, 0.0, 0.0)
        val UNIT = Vector3(1.0, 1.0, 1.0)
        val X_AXIS = Vector3(1.0, 0.0, 0.0)
        val Y_AXIS = Vector3(0.0, 1.0, 0.0)
        val Z_AXIS = Vector3(0.0, 0.0, 1.0)
    }
}
