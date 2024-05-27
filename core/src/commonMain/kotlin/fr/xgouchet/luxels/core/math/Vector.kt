package fr.xgouchet.luxels.core.math

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Represents a vector in an n-dimensional space coordinates.
 * @param D the dimension of the vector
 * @property data the internal representation of the vector
 */
@Suppress("TooManyFunctions")
class Vector<D : Dimension> internal constructor(
    val data: DoubleArray,
) {

    constructor(data: Collection<Double>) : this(data.toDoubleArray())

    // region Access

    /**
     * @return the components as a list of Double
     */
    fun components(): List<Double> {
        return data.toList()
    }

    // endregion

    // region Operators

    /**
     * @param vector the vector to add to this vector
     * @return a vector with each component as the component in this vector
     * plus the matching component in the given input
     */
    operator fun plus(vector: Vector<D>): Vector<D> {
        return Vector(data.zip(vector.data) { a, b -> a + b })
    }

    /**
     * @param vector the vector to subtract from this vector
     * @return a vector with each component as the component in this vector
     * minus the matching component in the given input
     */
    operator fun minus(vector: Vector<D>): Vector<D> {
        return Vector(data.zip(vector.data) { a, b -> a - b })
    }

    /**
     * @return a vector with each component as the negated value
     * of the matching component in this vector
     */
    operator fun unaryMinus(): Vector<D> {
        return Vector(data.map { -it })
    }

    /**
     * @param vector the vector factor by which to multiply the vector
     * @return a vector with each component as the component in this vector
     * multiplied by the matching component in the given input
     */
    operator fun times(vector: Vector<D>): Vector<D> {
        return Vector(data.zip(vector.data) { a, b -> a * b })
    }

    /**
     * @param scale the factor by which to multiply the vector
     * @return a vector with each component multiplied by the given input
     */
    operator fun times(scale: Double): Vector<D> {
        return Vector(data.map { it * scale })
    }

    /**
     * @param vector the vector factor by which to divide the vector
     * @return a vector with each component as the component in this vector
     * divided by the matching component in the given input
     */
    operator fun div(vector: Vector<D>): Vector<D> {
        return Vector(data.zip(vector.data) { a, b -> a / b })
    }

    /**
     * @param scale the factor by which to divide the vector
     * @return a vector with each component divided by the given input
     */
    operator fun div(scale: Double): Vector<D> {
        return Vector(data.map { it / scale })
    }

    /**
     * @param i the index of a component in this vector
     * @return the component at index i
     */
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

    /**
     * Refracts this vector through a surface.
     * @param normal the normal vector of the surface through which the vector should be refracted
     * @param sourceIndex the source medium refraction index
     * @param destinationIndex the destination medium refraction index
     * @param refractionFactor the refraction factor
     */
    fun refract(
        normal: Vector<D>,
        sourceIndex: Double,
        destinationIndex: Double,
        refractionFactor: Double,
    ): Vector<D> {
        val r = (sourceIndex / destinationIndex) * refractionFactor
        val c = -(normal.dot(this))
        val k = r * c - sqrt(kotlin.math.abs(1.0 - (r * r * (1.0 - c * c))))
        return (this * r) + (normal * k)
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

    /**
     * Converts this vector into an horizontal matrix.
     * @return the matrix with in row of n values
     */
    fun asHorizontalMatrix(): Matrix<D, Dimension.D1> {
        return Matrix(
            data = data.copyOf(),
            width = data.size,
            height = 1,
        )
    }

    /**
     * Converts this vector into an vertical matrix.
     * @return the matrix with in column of n values
     */
    fun asVerticalMatrix(): Matrix<Dimension.D1, D> {
        return Matrix(
            data = data.copyOf(),
            width = 1,
            height = data.size,
        )
    }

    // endregion

    // region Any

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
        return "Vector<${data.size}> [${data.joinToString()}]"
    }

    // endregion

    companion object {

        /**
         * Create the axes vectors corresponding to the provided dimension.
         * @param D the dimension of the vectors
         * @param d an instance of the dimension
         * @return a list of base axes vectors
         */
        fun <D : Dimension> axes(d: D): List<Vector<D>> {
            return d.range.map { i ->
                Vector(DoubleArray(d.size) { if (it == i) 1.0 else 0.0 })
            }
        }

        /**
         * Create a nul vector.
         * @param D the dimension of the vectors
         * @param d an instance of the dimension
         * @return a vector with all components set to 0
         */
        fun <D : Dimension> nul(d: D): Vector<D> {
            return Vector(DoubleArray(d.size) { 0.0 })
        }

        /**
         * Create a unit vector.
         * @param D the dimension of the vectors
         * @param d an instance of the dimension
         * @return a vector with all components set to 1
         */
        fun <D : Dimension> unit(d: D): Vector<D> {
            return Vector(DoubleArray(d.size) { 1.0 })
        }
    }
}
