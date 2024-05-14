package fr.xgouchet.luxels.core.math

import kotlin.jvm.JvmStatic
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

class Vector<D : Dimension> internal constructor(
    val data: DoubleArray,
) {

    internal constructor(data: Array<Double>) : this(data.toDoubleArray())

    internal constructor(data: Collection<Double>) : this(data.toDoubleArray())

    // region components

    /**
     * @return the components as a list of Double
     */
    fun components(): List<Double> {
        return data.toList()
    }

    // endregion

    // region Operators

    operator fun plus(vector: Vector<D>): Vector<D> {
        return Vector(data.zip(vector.data) { a, b -> a + b })
    }

    operator fun minus(vector: Vector<D>): Vector<D> {
        return Vector(data.zip(vector.data) { a, b -> a - b })
    }

    operator fun unaryMinus(): Vector<D> {
        return Vector(data.map { -it })
    }

    operator fun times(vector: Vector<D>): Vector<D> {
        return Vector(data.zip(vector.data) { a, b -> a * b })
    }

    operator fun times(scale: Double): Vector<D> {
        return Vector(data.map { it * scale })
    }

    operator fun div(vector: Vector<D>): Vector<D> {
        return Vector(data.zip(vector.data) { a, b -> a / b })
    }

    operator fun div(scale: Double): Vector<D> {
        return Vector(data.map { it / scale })
    }

    operator fun get(i: Int): Double {
        require(i < data.size)
        return data[i]
    }

    // endregion

    // region Geometry

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
        return data.sumOf { it * it }
    }

    /**
     * @return a vector colinear with this vector, with it's length normalized to 1
     */
    fun normalized(): Vector<D> {
        val length = length()
        return this / length
    }

    /**
     * @param other the vector with which to compute the dot product
     * @return the dot product between this vector and the given vector
     */
    infix fun dot(other: Vector<D>): Double {
        return data.zip(other.data) { a, b -> a * b }.sum()
    }

    /**
     * Reflect this vector against a surface with the given normal.
     * @param normal the normal vector of the surface against which this vector should be reflected
     * @return the reflected vector
     */
    fun reflect(normal: Vector<D>): Vector<D> {
        return this - (normal * dot(normal) * 2.0)
    }

    // endregion

    // region Utils

    /**
     * @return a vector with each component as the floor value of the matching component in this vector
     */
    fun floor(): Vector<D> {
        return Vector(data.map { floor(it) })
    }

    /**
     * @return a vector with each component as the ceil value of the matching component in this vector
     */
    fun ceil(): Vector<D> {
        return Vector(data.map { ceil(it) })
    }

    /**
     * @return a vector with each component as the absolute value of the matching component in this vector
     */
    fun abs(): Vector<D> {
        return Vector(data.map { abs(it) })
    }

    /**
     * @return true if at least one component of this vector is NaN
     */
    fun isNaN(): Boolean {
        return data.any { it.isNaN() }
    }

    /**
     * @return true if at least one component of this vector is plus or minus infinity
     */
    fun isInfinite(): Boolean {
        return data.any { it.isInfinite() }
    }

    /**
     * @param other a vector to compare to
     * @return true if at all components of this vector are less than the components in the other vector
     */
    infix fun isLessThan(other: Vector<D>): Boolean {
        return data.zip(other.data) { a, b -> a < b }.all { it }
    }

    /**
     * @param other a vector to compare to
     * @return true if at all components of this vector are less than or equal the components in the other vector
     */
    infix fun isLessThanOrEqual(other: Vector<D>): Boolean {
        return data.zip(other.data) { a, b -> a <= b }.all { it }
    }

    /**
     * @param other a vector to compare to
     * @return true if at all components of this vector are greater than the components in the other vector
     */
    infix fun isGreaterThan(other: Vector<D>): Boolean {
        return data.zip(other.data) { a, b -> a > b }.all { it }
    }

    /**
     * @param other a vector to compare to
     * @return true if at all components of this vector are greater than or equal the components in the other vector
     */
    infix fun isGreaterThanOrEqual(other: Vector<D>): Boolean {
        return data.zip(other.data) { a, b -> a >= b }.all { it }
    }

    // endregion

    // region Object

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Vector<*>

        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    override fun toString(): String {
        return "Vector [${data.joinToString()}]"
    }

    // endregion

    companion object {

        @JvmStatic
        fun `1D`(x: Double): Vector<Dimension.D1> {
            return Vector(arrayOf(x))
        }

        @JvmStatic
        fun `2D`(x: Double, y: Double): Vector<Dimension.D2> {
            return Vector(arrayOf(x, y))
        }

        @JvmStatic
        fun `3D`(x: Double, y: Double, z: Double): Vector<Dimension.D3> {
            return Vector(arrayOf(x, y))
        }

        @JvmStatic
        fun `4D`(x: Double, y: Double, z: Double, w: Double): Vector<Dimension.D4> {
            return Vector(arrayOf(x, y, z, w))
        }

        @JvmStatic
        fun `5D`(x: Double, y: Double, z: Double, w: Double): Vector<Dimension.D5> {
            return Vector(arrayOf(x, y, z, w))
        }

        @JvmStatic
        fun `6D`(x: Double, y: Double, z: Double, w: Double): Vector<Dimension.D6> {
            return Vector(arrayOf(x, y, z, w))
        }

        @JvmStatic
        fun <D : Dimension> axes(d: D): List<Vector<D>> {
            return d.range.map { i ->
                Vector(DoubleArray(d.size) { if (it == i) 1.0 else 0.0 })
            }
        }

        @JvmStatic
        fun <D : Dimension> nul(d: D): Vector<D> {
            return Vector(DoubleArray(d.size) { 0.0 })
        }

        @JvmStatic
        fun <D : Dimension> unit(d: D): Vector<D> {
            return Vector(DoubleArray(d.size) { 1.0 })
        }
    }
}
