package fr.xgouchet.luxels.core.math

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Represents a vector in a 3D space coordinates.
 * @property x the x component
 * @property y the y component
 * @property z the z component
 */
@Suppress("TooManyFunctions")
data class Vector3(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    /** The [Vector2] corresponding with the [x, y] components. */
    val xy: Vector2 by lazy { Vector2(x, y) }

    /** The [Vector2] corresponding with the [x, z] components. */
    val xz: Vector2 by lazy { Vector2(x, z) }

    /** The [Vector2] corresponding with the [y, z] components. */
    val yz: Vector2 by lazy { Vector2(y, z) }

    // region Operators

    /**
     * @param vector the vector to add to this vector
     * @return a vector with each component as the component in this vector plus the matching component in the given input
     */
    operator fun plus(vector: Vector3): Vector3 {
        return Vector3(
            x + vector.x,
            y + vector.y,
            z + vector.z,
        )
    }

    /**
     * @param vector the vector to subtract from this vector
     * @return a vector with each component as the component in this vector minus the matching component in the given input
     */
    operator fun minus(vector: Vector3): Vector3 {
        return Vector3(
            x - vector.x,
            y - vector.y,
            z - vector.z,
        )
    }

    /**
     * @param scale the factor by which to multiply the vector
     * @return a vector with each component multiplied by the given input
     */
    operator fun times(scale: Double): Vector3 {
        return Vector3(
            x * scale,
            y * scale,
            z * scale,
        )
    }

    /**
     * @param scale the vector factor by which to multiply the vector
     * @return a vector with each component as the component in this vector multiplied by the matching component in the given input
     */
    operator fun times(scale: Vector3): Vector3 {
        return Vector3(
            x * scale.x,
            y * scale.y,
            z * scale.z,
        )
    }

    /**
     * @param scale the factor by which to divide the vector
     * @return a vector with each component divided by the given input
     */
    operator fun div(scale: Double): Vector3 {
        return Vector3(
            x / scale,
            y / scale,
            z / scale,
        )
    }

    /**
     * @param scale the vector factor by which to divide the vector
     * @return a vector with each component as the component in this vector divided by the matching component in the given input
     */
    operator fun div(scale: Vector3): Vector3 {
        return Vector3(
            x / scale.x,
            y / scale.y,
            z / scale.z,
        )
    }

    /**
     * @return a vector with each component as the negated value of the matching component in this vector
     */
    operator fun unaryMinus(): Vector3 {
        return Vector3(-x, -y, -z)
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
        return (x * x) + (y * y) + (z * z)
    }

    /**
     * @return a vector colinear with this vector, with it's length normalized to 1
     */
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

    /**
     * @param other the vector with which to compute the dot product
     * @return the dot product between this vector and the given vector
     */
    infix fun dot(other: Vector3): Double {
        return (x * other.x) + (y * other.y) + (z * other.z)
    }

    /**
     * @param other the vector with which to compute the cross product
     * @return the cross product between this vector and the given vector
     */
    infix fun cross(other: Vector3): Vector3 {
        return Vector3(
            (y * other.z) - (z * other.y),
            (z * other.x) - (x * other.z),
            (x * other.y) - (y * other.x),
        )
    }

    /**
     * @return a vector with each component as the floor value of the matching component in this vector
     */
    fun floor(): Vector3 {
        return Vector3(
            floor(x),
            floor(y),
            floor(z),
        )
    }

    /**
     * @return a vector with each component as the ceil value of the matching component in this vector
     */
    fun ceil(): Vector3 {
        return Vector3(
            ceil(x),
            ceil(y),
            ceil(z),
        )
    }

    /**
     * @return a vector with each component as the absolute value of the matching component in this vector
     */
    fun abs(): Vector3 {
        return Vector3(abs(x), abs(y), abs(z))
    }

    /**
     * Reflect this vector against a surface with the given normal.
     * @param normal the normal vector of the surface against which this vector should be reflected
     * @return the reflected vector
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

    /**
     * @return the components as a list of Double, with size 3, in [x, y, z] order
     */
    fun components(): List<Double> {
        return listOf(x, y, z)
    }

    /**
     * @return true if at least one component of this vector is NaN
     */
    fun isNaN(): Boolean {
        return x.isNaN() || y.isNaN() || z.isNaN()
    }

    // endregion

    companion object {

        /**
         * Builds a [Vector3] from the given components.
         * @param components a list of exactly 3 components in [x, y, z] order
         * @return the constructed vector
         */
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

        /** A null [Vector3], i.e.: a [Vector3] with all components set to 0.0. */
        val NULL = Vector3(0.0, 0.0, 0.0)

        /** A unit [Vector3], i.e.: a [Vector3] with all components set to 1.0. */
        val UNIT = Vector3(1.0, 1.0, 1.0)

        /** A [Vector3] corresponding to the X axis of the 3D space. */
        val X_AXIS = Vector3(1.0, 0.0, 0.0)

        /** A [Vector3] corresponding to the Y axis of the 3D space. */
        val Y_AXIS = Vector3(0.0, 1.0, 0.0)

        /** A [Vector3] corresponding to the Z axis of the 3D space. */
        val Z_AXIS = Vector3(0.0, 0.0, 1.0)
    }
}
