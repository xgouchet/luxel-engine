package fr.xgouchet.luxels.core.math

import kotlin.math.abs

/**
 * Represents a m×n Matrix representation.
 * @param C the dimension corresponding to the width (i.e.: the number of columns)
 * @param R the dimension corresponding to the height (i.e.: the number of rows)
 * @property data the internal representation of the matrix's cells
 * @property width the width of the matrix
 * @property height the height of the matrix
 */
@Suppress("TooManyFunctions")
class Matrix<C : Dimension, R : Dimension> internal constructor(
    internal val data: DoubleArray,
    internal val width: Int,
    internal val height: Int,
) {
    internal constructor(
        cols: C,
        rows: R,
    ) : this(
        data = DoubleArray(cols.size * rows.size) { 0.0 },
        width = cols.size,
        height = rows.size,
    )

    // region Operators

    /**
     * @return a matrix with each component as the negated value
     * of the matching component in this matrix
     */
    operator fun unaryMinus(): Matrix<C, R> {
        return Matrix(
            data = data.map { -it }.toDoubleArray(),
            width = width,
            height = height,
        )
    }

    /**
     * @param matrix the matrix to add to this matrix
     * @return a matrix with each component as the component in this matrix
     * plus the matching component in the given input
     */
    operator fun plus(matrix: Matrix<C, R>): Matrix<C, R> {
        check((matrix.width == width) and (matrix.height == height))

        return Matrix(
            data = data.zip(matrix.data) { a, b -> a + b }.toDoubleArray(),
            width = width,
            height = height,
        )
    }

    /**
     * @param matrix the matrix to subtract from this matrix
     * @return a matrix with each component as the component in this matrix
     * minus the matching component in the given input
     */
    operator fun minus(matrix: Matrix<C, R>): Matrix<C, R> {
        check((matrix.width == width) and (matrix.height == height))
        return Matrix(
            data = data.zip(matrix.data) { a, b -> a - b }.toDoubleArray(),
            width = width,
            height = height,
        )
    }

    /**
     * @param scale the factor by which to multiply the matrix
     * @return a matrix with each component multiplied by the given input
     */
    operator fun times(scale: Double): Matrix<C, R> {
        return Matrix(
            data = data.map { it * scale }.toDoubleArray(),
            width = width,
            height = height,
        )
    }

    /**
     * @param X the number of columns in the matrix to multiply with
     * @param matrix a matrix to multiply with. Note that the number of rows must match the
     * number of columns in this matrix.
     * @return a matrix as the result of the matrix multiplication of this with the input
     */
    operator fun <X : Dimension> times(matrix: Matrix<X, C>): Matrix<X, R> {
        check(matrix.height == width)
        val result = Matrix<X, R>(
            data = DoubleArray(matrix.width * height),
            width = matrix.width,
            height = height,
        )

        for (lj in 0..<height) {
            for (ri in 0..<matrix.width) {
                var sum = 0.0
                for (k in 0..<matrix.height) {
                    val l = get(k, lj)
                    val r = matrix.get(ri, k)
                    sum += (l * r)
                }
                result.set(ri, lj, sum)
            }
        }

        return result
    }

    // endregion

    // region Math

    /**
     * @return the transpose of this matrix (i.e. the matrix flipped across the diagonal)
     */
    fun transpose(): Matrix<R, C> {
        return Matrix(
            data = DoubleArray(data.size) { idx ->
                val i = (idx.mod(height))
                val j = (idx - i) / height
                data[j + (i * width)]
            },
            width = height,
            height = width,
        )
    }

    /**
     * @return the determinant of this matrix, or null if this matrix isn't square.
     */
    fun determinantOrNull(): Double? {
        return if (width != height) null else determinant()
    }

    /**
     * @return the determinant of this matrix.
     * @throws UnsupportedOperationException when called on a non square matrix
     */
    fun determinant(): Double {
        if (width != height) {
            throw UnsupportedOperationException("Cannot compute the determinant of a non square matrix")
        }

        return determinant(data, width)
    }

    /**
     * @return true if this matrix is square and can be inverted
     */
    fun isInvertible(): Boolean {
        return if (width != height) {
            false
        } else {
            abs(determinant()) > EPSILON
        }
    }

    /**
     * @return the inverse of this matrix, or null if this matrix can't be inverted.
     */
    fun inverseOrNull(): Matrix<R, C>? {
        return if (isInvertible()) inverse() else null
    }

    /**
     * @return the inverse of this matrix
     * @throws UnsupportedOperationException when called on a non square matrix
     */
    @Suppress("CyclomaticComplexMethod", "CognitiveComplexMethod", "NestedBlockDepth")
    fun inverse(): Matrix<R, C> {
        if (width != height) {
            throw UnsupportedOperationException("Cannot compute the inverse of a non square matrix")
        }

        if (width == 1) {
            return Matrix(DoubleArray(1) { 1.0 / data[0] }, 1, 1)
        }

        val reduced = data.copyOf()
        val inverse = DoubleArray(width * height) { idx ->
            if (idx.mod(height + 1) == 0) 1.0 else 0.0
        }

        // gaussian reduction step 1: Make reduced a diagonal matrix
        for (i in 0..<width) {
            var pivot = reduced[(i * width) + i]

            // Special case, swap rows if needed
            if (abs(pivot) <= EPSILON) {
                var swapWithRowIdx = -1
                for (s in (i + 1)..<height) {
                    val tentativePivot = reduced[(s * width) + i]
                    if (abs(tentativePivot) > EPSILON) {
                        swapWithRowIdx = s
                        break
                    }
                }
                if (swapWithRowIdx < 0) {
                    throw UnsupportedOperationException("Unable to invert matrix")
                } else {
                    pivot = reduced[(swapWithRowIdx * width) + i]
                    // swap rows
                    for (k in 0..<width) {
                        val tempR = reduced[(i * width) + k]
                        reduced[(i * width) + k] = reduced[(swapWithRowIdx * width) + k]
                        reduced[(swapWithRowIdx * width) + k] = tempR
                        val tempI = inverse[(i * width) + k]
                        inverse[(i * width) + k] = inverse[(swapWithRowIdx * width) + k]
                        inverse[(swapWithRowIdx * width) + k] = tempI
                    }
                }
            }

            for (j in 0..<height) {
                if (i == j) {
                    // ignore
                } else {
                    val factor = -(reduced[(j * width) + i] / pivot)

                    for (k in 0..<width) {
                        reduced[(j * width) + k] += reduced[(i * width) + k] * factor
                        inverse[(j * width) + k] += inverse[(i * width) + k] * factor
                    }
                }
            }
        }

        // gaussian reduction step 2: Make reduced identity
        for (j in 0..<height) {
            val factor = 1.0 / reduced[(j * width) + j]
            for (k in 0..<width) {
                reduced[(j * width) + k] *= factor
                inverse[(j * width) + k] *= factor
            }
        }

        return Matrix(inverse, height, width)
    }

    // endregion

    // region Access

    /**
     * @param i the index of the column
     * @param j the index of the row
     */
    fun get(i: Int, j: Int): Double {
        check(i < width) { "Can't access column $i in get($j, $i) in $this" }
        check(j < height) { "Can't access row $j in get($j, $i) in $this" }
        return data[(j * width) + i]
    }

    /**
     * @param i the index of the column
     * @param j the index of the row
     * @param value the new value
     */
    fun set(
        i: Int,
        j: Int,
        value: Double,
    ) {
        check(i < width) { "Can't access column $i in set($j, $i) in $this" }
        check(j < height) { "Can't access row $j in set($j, $i) in $this" }
        data[(j * width) + i] = value
    }

    // endregion

    // region Any

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Matrix<*, *>

        if (!data.contentEquals(other.data)) return false
        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }

    override fun toString(): String {
        return "Matrix<$width, $height> [${data.joinToString()}]"
    }

    // endregion

    companion object {

        /**
         * @param C the column dimension
         * @param R the row dimension
         * @param cols the column dimension
         * @param rows the row dimension
         * @return a matrix filled with 0
         */
        fun <C : Dimension, R : Dimension> zero(cols: C, rows: R): Matrix<C, R> {
            return Matrix(
                data = DoubleArray(cols.size * rows.size) { 0.0 },
                width = cols.size,
                height = rows.size,
            )
        }

        /**
         * @param C the column dimension
         * @param R the row dimension
         * @param cols the column dimension
         * @param rows the row dimension
         * @return a matrix filled with 1
         */
        fun <C : Dimension, R : Dimension> one(cols: C, rows: R): Matrix<C, R> {
            return Matrix(
                data = DoubleArray(cols.size * rows.size) { 1.0 },
                width = cols.size,
                height = rows.size,
            )
        }

        /**
         * @param C the column dimension
         * @param R the row dimension
         * @param cols the column dimension
         * @param rows the row dimension
         * @return an identity matrix
         */
        fun <C : Dimension, R : Dimension> identity(cols: C, rows: R): Matrix<C, R> {
            return Matrix(
                data = DoubleArray(cols.size * rows.size) { idx ->
                    if (idx.mod(cols.size + 1) == 0) {
                        1.0
                    } else {
                        0.0
                    }
                },
                width = cols.size,
                height = rows.size,
            )
        }

        // region Internal

        private fun determinant(data: DoubleArray, size: Int): Double {
            // Simplest case
            if (size == 1) return data[0]

            if (size == 2) return (data[0] * data[3]) - (data[1] * data[2])

            // General case (recursive)
            var sum = 0.0
            var sign = 1.0
            for (i in 0..<size) {
                val k = data[i]
                val sm = minor(data, size, size, i, 0)
                sum += (sign * k * determinant(sm, size - 1))
                sign *= -1
            }

            return sum
        }

        private fun minor(
            data: DoubleArray,
            width: Int,
            height: Int,
            removeCol: Int,
            removeRow: Int,
        ): DoubleArray {
            // i = column idx
            // j = row indx
            // data[(j * width) + i]
            val subWidth = width - 1
            val subHeight = height - 1
            val result = DoubleArray(subWidth * subHeight) { 0.0 }

            for (i in 0..<subWidth) {
                val orgI = if (i >= removeCol) i + 1 else i
                for (j in 0..<subHeight) {
                    val orgJ = if (j >= removeRow) j + 1 else j
                    result[(j * subWidth) + i] = data[(orgJ * width) + orgI]
                }
            }

            return result
        }

        // endregion
    }
}
