package fr.xgouchet.luxels.core.math

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Represents a vector in a 2D space coordinates.
 * @property x the x component
 * @property y the y component
 */
@Suppress("TooManyFunctions")
data class Vector2(
    val x: Double,
    val y: Double,
) {

    // region Operators

    /**
     * @param vector the vector to add to this vector
     * @return a vector with each component as the component in this vector plus the matching component in the given input
     */
    operator fun plus(vector: Vector2): Vector2 {
        return Vector2(
            x + vector.x,
            y + vector.y,
        )
    }

    /**
     * @param vector the vector to subtract from this vector
     * @return a vector with each component as the component in this vector minus the matching component in the given input
     */
    operator fun minus(vector: Vector2): Vector2 {
        return Vector2(
            x - vector.x,
            y - vector.y,
        )
    }

    /**
     * @param scale the factor by which to multiply the vector
     * @return a vector with each component multiplied by the given input
     */
    operator fun times(scale: Double): Vector2 {
        return Vector2(
            x * scale,
            y * scale,
        )
    }

    /**
     * @param scale the vector factor by which to multiply the vector
     * @return a vector with each component as the component in this vector multiplied by the matching component in the given input
     */
    operator fun times(scale: Vector2): Vector2 {
        return Vector2(
            x * scale.x,
            y * scale.y,
        )
    }

    /**
     * @param scale the factor by which to divide the vector
     * @return a vector with each component divided by the given input
     */
    operator fun div(scale: Double): Vector2 {
        return Vector2(
            x / scale,
            y / scale,
        )
    }

    /**
     * @param scale the vector factor by which to divide the vector
     * @return a vector with each component as the component in this vector divided by the matching component in the given input
     */
    operator fun div(scale: Vector2): Vector2 {
        return Vector2(
            x / scale.x,
            y / scale.y,
        )
    }

    /**
     * @return a vector with each component as the negated value of the matching component in this vector
     */
    operator fun unaryMinus(): Vector2 {
        return Vector2(-x, -y)
    }

    // endregion

    // region Length

    /**
     * @return the length of this vector
     */
    fun length(): Double {
        return sqrt(squaredLength())
    }

    /**
     * @return the length of this vector, squared
     */
    fun squaredLength(): Double {
        return (x * x) + (y * y)
    }

    /**
     * @return a vector colinear with this vector, with it's length normalized to 1
     */
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

    /**
     * @param other the vector with which to compute the dot product
     * @return the dot product between this vector and the given vector
     */
    infix fun dot(other: Vector2): Double {
        return (x * other.x) + (y * other.y)
    }

    /**
     * @return a vector with each component as the floor value of the matching component in this vector
     */
    fun floor(): Vector2 {
        return Vector2(
            floor(x),
            floor(y),
        )
    }

    /**
     * @return a vector with each component as the ceil value of the matching component in this vector
     */
    fun ceil(): Vector2 {
        return Vector2(
            ceil(x),
            ceil(y),
        )
    }

    /**
     * @return a vector with each component as the absolute value of the matching component in this vector
     */
    fun abs(): Vector2 {
        return Vector2(abs(x), abs(y))
    }

    /**
     * Reflect this vector against a surface with the given normal.
     * @param normal the normal vector of the surface against which this vector should be reflected
     * @return the reflected vector
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

    /**
     * @return the components as a list of Double, with size 2, in [x, y] order
     */
    fun components(): List<Double> {
        return listOf(x, y)
    }

    /**
     * @return true if at least one component of this vector is NaN
     */
    fun isNaN(): Boolean {
        return x.isNaN() || y.isNaN()
    }

    // endregion

    companion object {

        /**
         * Builds a [Vector2] from the given components.
         * @param components a list of exactly 2 components in [x, y] order
         * @return the constructed vector
         */
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

        /** A null [Vector2], i.e.: a [Vector2] with all components set to 0.0. */
        val NULL = Vector2(0.0, 0.0)

        /** A unit [Vector2], i.e.: a [Vector2] with all components set to 1.0. */
        val UNIT = Vector2(1.0, 1.0)

        /** A [Vector2] corresponding to the X axis of the 2D space. */
        val X_AXIS = Vector2(1.0, 0.0)

        /** A [Vector2] corresponding to the Y axis of the 2D space. */
        val Y_AXIS = Vector2(0.0, 1.0)
    }
}
