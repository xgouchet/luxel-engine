package fr.xgouchet.luxels.core.math

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Represents a vector in a 2D space coordinates.
 */
@Suppress("TooManyFunctions")
data class Vector2(
    val x: Double,
    val y: Double,
) {

    // region Operators

    operator fun plus(vector: Vector2): Vector2 {
        return Vector2(
            x + vector.x,
            y + vector.y,
        )
    }

    operator fun minus(vector: Vector2): Vector2 {
        return Vector2(
            x - vector.x,
            y - vector.y,
        )
    }

    operator fun times(scale: Double): Vector2 {
        return Vector2(
            x * scale,
            y * scale,
        )
    }

    operator fun times(scale: Vector2): Vector2 {
        return Vector2(
            x * scale.x,
            y * scale.y,
        )
    }

    operator fun div(scale: Double): Vector2 {
        return Vector2(
            x / scale,
            y / scale,
        )
    }

    operator fun div(scale: Vector2): Vector2 {
        return Vector2(
            x / scale.x,
            y / scale.y,
        )
    }

    operator fun unaryMinus(): Vector2 {
        return Vector2(-x, -y)
    }

    // endregion

    // region Length

    fun length(): Double {
        return sqrt(squaredLength())
    }

    fun squaredLength(): Double {
        return (x * x) + (y * y)
    }

    fun normalized(): Vector2 {
        val length = length()
        return if (length >= EPSILON) {
            Vector2(
                x / length,
                y / length,
            )
        } else {
            // TODO use NaN ?
            this
        }
    }

    // endregion

    // region Geometry

    infix fun dot(other: Vector2): Double {
        return (x * other.x) + (y * other.y)
    }

    fun floor(): Vector2 {
        return Vector2(
            floor(x),
            floor(y),
        )
    }

    fun ceil(): Vector2 {
        return Vector2(
            ceil(x),
            ceil(y),
        )
    }

    fun abs(): Vector2 {
        return Vector2(abs(x), abs(y))
    }

    /**
     * Reflect this vector against a surface with the given normal
     */
    fun reflect(
        normal: Vector2,
    ): Vector2 {
        return this - (normal * dot(normal) * 2.0)
    }

    /**
     * Refract this vector through a surface with the given normal.
     */
    fun refract(
        normal: Vector2,
        sourceIndex: Double,
        destinationIndex: Double,
        refractionFactor: Double,
    ): Vector2 {
        val r = (sourceIndex / destinationIndex) * refractionFactor
        val c = -(normal.dot(this))
        val k = r * c - sqrt(abs(1.0 - (r * r * (1.0 - c * c))))
        return (this * r) + (normal * k)
    }

    // endregion

    // region Utils

    fun components(): List<Double> {
        return listOf(x, y)
    }

    fun isNaN(): Boolean {
        return x.isNaN() || y.isNaN()
    }

    // endregion

    companion object {
        fun fromComponents(components: List<Double>): Vector2 {
            check(components.size == 2)
            return Vector2(components[0], components[1])
        }

        /**
         * @param polar (between 0 and 2π)
         * @param radius (between 0 and ∞)
         */
        fun fromCircular(polar: Double, radius: Double = 1.0): Vector2 {
            return Vector2(
                radius * cos(polar),
                radius * sin(polar),
            )
        }

        val NULL = Vector2(0.0, 0.0)
        val UNIT = Vector2(1.0, 1.0)

        val X_AXIS = Vector2(1.0, 0.0)
        val Y_AXIS = Vector2(0.0, 1.0)
    }
}
