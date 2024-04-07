package fr.xgouchet.luxels.core.math.geometry

import fr.xgouchet.luxels.core.math.EPSILON
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Represents a vector in a 4D space coordinates.
 * @property x the x component
 * @property y the y component
 * @property z the z component
 * @property w the w component
 */
@Suppress("TooManyFunctions")
data class Vector4(
    val x: Double,
    val y: Double,
    val z: Double,
    val w: Double,
) : Vector {

    /** The [Vector2] corresponding with the [x, y] components. */
    val xy: Vector2 by lazy { Vector2(x, y) }

    /** The [Vector2] corresponding with the [x, z] components. */
    val xz: Vector2 by lazy { Vector2(x, z) }

    /** The [Vector2] corresponding with the [x, w] components. */
    val xw: Vector2 by lazy { Vector2(x, w) }

    /** The [Vector2] corresponding with the [y, z] components. */
    val yz: Vector2 by lazy { Vector2(y, z) }

    /** The [Vector2] corresponding with the [y, w] components. */
    val yw: Vector2 by lazy { Vector2(y, w) }

    /** The [Vector2] corresponding with the [z, w] components. */
    val zw: Vector2 by lazy { Vector2(z, w) }

    /** The [Vector3] corresponding with the [x, y, z] components. */
    val xyz: Vector3 by lazy { Vector3(x, y, z) }

    /** The [Vector3] corresponding with the [x, y, w] components. */
    val xyw: Vector3 by lazy { Vector3(x, y, w) }

    /** The [Vector3] corresponding with the [x, z, w] components. */
    val xzw: Vector3 by lazy { Vector3(x, z, w) }

    /** The [Vector3] corresponding with the [y, z, w] components. */
    val yzw: Vector3 by lazy { Vector3(y, z, w) }

    // region Operators

    /**
     * @param vector the vector to add to this vector
     * @return a vector with each component as the component in this vector plus the matching component in the given input
     */
    operator fun plus(vector: Vector4): Vector4 {
        return Vector4(
            x + vector.x,
            y + vector.y,
            z + vector.z,
            w + vector.w,
        )
    }

    /**
     * @param vector the vector to subtract from this vector
     * @return a vector with each component as the component in this vector minus the matching component in the given input
     */
    operator fun minus(vector: Vector4): Vector4 {
        return Vector4(
            x - vector.x,
            y - vector.y,
            z - vector.z,
            w - vector.w,
        )
    }

    /**
     * @param scale the factor by which to multiply the vector
     * @return a vector with each component multiplied by the given input
     */
    operator fun times(scale: Double): Vector4 {
        return Vector4(
            x * scale,
            y * scale,
            z * scale,
            w * scale,
        )
    }

    /**
     * @param scale the vector factor by which to multiply the vector
     * @return a vector with each component as the component in this vector multiplied by the matching component in the given input
     */
    operator fun times(scale: Vector4): Vector4 {
        return Vector4(
            x * scale.x,
            y * scale.y,
            z * scale.z,
            w * scale.w,
        )
    }

    /**
     * @param scale the factor by which to divide the vector
     * @return a vector with each component divided by the given input
     */
    operator fun div(scale: Double): Vector4 {
        return Vector4(
            x / scale,
            y / scale,
            z / scale,
            w / scale,
        )
    }

    /**
     * @param scale the vector factor by which to divide the vector
     * @return a vector with each component as the component in this vector divided by the matching component in the given input
     */
    operator fun div(scale: Vector4): Vector4 {
        return Vector4(
            x / scale.x,
            y / scale.y,
            z / scale.z,
            w / scale.w,
        )
    }

    /**
     * @return a vector with each component as the negated value of the matching component in this vector
     */
    operator fun unaryMinus(): Vector4 {
        return Vector4(-x, -y, -z, -w)
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
        return (x * x) + (y * y) + (z * z) + (w * w)
    }

    /**
     * @return a vector colinear with this vector, with it's length normalized to 1
     */
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

    /**
     * @param other the vector with which to compute the dot product
     * @return the dot product between this vector and the given vector
     */
    infix fun dot(other: Vector4): Double {
        return (x * other.x) + (y * other.y) + (z * other.z) + (w * other.w)
    }

    /**
     * @return a vector with each component as the floor value of the matching component in this vector
     */
    fun floor(): Vector4 {
        return Vector4(
            floor(x),
            floor(y),
            floor(z),
            floor(w),
        )
    }

    /**
     * @return a vector with each component as the ceil value of the matching component in this vector
     */
    fun ceil(): Vector4 {
        return Vector4(
            ceil(x),
            ceil(y),
            ceil(z),
            ceil(w),
        )
    }

    /**
     * @return a vector with each component as the absolute value of the matching component in this vector
     */
    fun abs(): Vector4 {
        return Vector4(abs(x), abs(y), abs(z), abs(w))
    }

    /**
     * Reflect this vector against a surface with the given normal.
     * @param normal the normal vector of the surface against which this vector should be reflected
     * @return the reflected vector
     */
    fun reflect(normal: Vector4): Vector4 {
        return this - (normal * dot(normal) * 2.0)
    }

    /**
     * Refract this vector through a surface with the given normal.
     */
    fun refract(normal: Vector4, sourceIndex: Double, destinationIndex: Double, refractionFactor: Double): Vector4 {
        val r = (sourceIndex / destinationIndex) * refractionFactor
        val c = -(normal.dot(this))
        val k = r * c - sqrt(abs(1.0 - (r * r * (1.0 - c * c))))
        return (this * r) + (normal * k)
    }

    // endregion

    // region Vector

    /**
     * @return the components as a list of Double, with size 4, in [x, y, z, w] order
     */
    override fun components(): List<Double> {
        return listOf(x, y, z, w)
    }

    // endregion

    /**
     * The builder instance for Vector2.
     */
    object Builder : Vector.Builder<Vector4> {
        override fun buildFromComponents(components: List<Double>): Vector4 {
            return fromComponents(components)
        }
    }

    companion object {
        /**
         * Builds a [Vector4] from the given components.
         * @param components a list of exactly 4 components in [x, y, z, w] order
         * @return the constructed vector
         */
        fun fromComponents(components: List<Double>): Vector4 {
            check(components.size == 4)
            return Vector4(components[0], components[1], components[2], components[3])
        }

        /** A null [Vector4], i.e.: a [Vector4] with all components set to 0.0. */
        val NULL = Vector4(0.0, 0.0, 0.0, 0.0)

        /** A unit [Vector4], i.e.: a [Vector4] with all components set to 1.0. */
        val UNIT = Vector4(1.0, 1.0, 1.0, 1.0)

        /** A [Vector4] corresponding to the X axis of the 4D space. */
        val X_AXIS = Vector4(1.0, 0.0, 0.0, 0.0)

        /** A [Vector4] corresponding to the Y axis of the 4D space. */
        val Y_AXIS = Vector4(0.0, 1.0, 0.0, 0.0)

        /** A [Vector4] corresponding to the Z axis of the 4D space. */
        val Z_AXIS = Vector4(0.0, 0.0, 1.0, 0.0)

        /** A [Vector4] corresponding to the W axis of the 4D space. */
        val W_AXIS = Vector4(0.0, 0.0, 0.0, 1.0)
    }
}
