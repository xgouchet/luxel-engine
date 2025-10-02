package art.luxels.core.math

import kotlin.math.abs
import kotlin.math.min

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

        for (leftRow in 0..<height) {
            for (rightCol in 0..<matrix.width) {
                var sum = 0.0
                for (k in 0..<matrix.height) {
                    val l = data[idx(k, leftRow)]
                    val r = matrix.get(rightCol, k)
                    sum += (l * r)
                }
                result.set(rightCol, leftRow, sum)
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
                data[(i * width) + j]
            },
            width = height,
            height = width,
        )
    }

    /**
     * @return the determinant of this matrix.
     * @throws UnsupportedOperationException when called on a non-square matrix
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
            val (i, j) = coords(idx)
            if (i == j) 1.0 else 0.0
        }

        // gaussian reduction step 1: Make reduced a diagonal matrix
        for (i in 0..<width) {
            var pivot = reduced[idx(i, i)]

            // Special case, swap rows if needed
            if (abs(pivot) <= EPSILON) {
                var swapWithRowIdx = -1
                for (s in (i + 1)..<height) {
                    val tentativePivot = reduced[idx(i, s)]
                    if (abs(tentativePivot) > EPSILON) {
                        swapWithRowIdx = s
                        break
                    }
                }
                if (swapWithRowIdx < 0) {
                    throw UnsupportedOperationException("Unable to invert matrix")
                } else {
                    pivot = reduced[idx(i, swapWithRowIdx)]
                    // swap rows
                    for (k in 0..<width) {
                        val tempR = reduced[idx(k, i)]
                        reduced[idx(k, i)] = reduced[idx(k, swapWithRowIdx)]
                        reduced[idx(k, swapWithRowIdx)] = tempR
                        val tempI = inverse[idx(k, i)]
                        inverse[idx(k, i)] = inverse[idx(k, swapWithRowIdx)]
                        inverse[idx(k, swapWithRowIdx)] = tempI
                    }
                }
            }

            for (j in 0..<height) {
                if (i == j) {
                    // ignore
                } else {
                    val factor = -(reduced[idx(i, j)] / pivot)

                    for (k in 0..<width) {
                        reduced[idx(k, j)] += reduced[idx(k, i)] * factor
                        inverse[idx(k, j)] += inverse[idx(k, i)] * factor
                    }
                }
            }
        }

        // gaussian reduction step 2: Make reduced identity
        for (j in 0..<height) {
            val factor = 1.0 / reduced[idx(j, j)]
            for (k in 0..<width) {
                reduced[idx(k, j)] *= factor
                inverse[idx(k, j)] *= factor
            }
        }

        return Matrix(inverse, height, width)
    }

    /**
     * Computes the row canonical form of this Matrix, through gaussian elimination.
     * @return a Matrix corresponding to this in reduced row echelon form
     */
    fun reducedRowEchelonForm(): Matrix<C, R> {
        val echelon = Matrix<C, R>(data.copyOf(), width, height)
        var marchingCol = 0
        var marchingRow = 0

        while ((marchingCol < width) && (marchingRow < height)) {
            // 1. Swap pivot
            val pivot = echelon.rrefEnsurePivot(marchingRow, marchingCol)
            if (abs(pivot) < EPSILON) {
                marchingCol++
            } else {
                // 2. scale row
                echelon.rrefScaleRow(marchingRow, marchingCol)

                // 3. Use row replacement
                echelon.rrefReduceOtherRows(marchingRow, marchingCol)

                marchingRow++
                marchingCol++
            }
        }

        return echelon
    }

    // endregion

    // region Access

    /**
     * @param col the index of the column
     * @param row the index of the row
     */
    fun get(col: Int, row: Int): Double {
        check(col in 0..<width) { "Can't access column $col in get($row, $col) in $this" }
        check(row in 0..<height) { "Can't access row $row in get($row, $col) in $this" }
        return data[idx(col, row)]
    }

    /**
     * @param col the index of the column
     * @param row the index of the row
     * @param value the new value
     */
    fun set(
        col: Int,
        row: Int,
        value: Double,
    ) {
        check(col in 0..<width) { "Can't access column $col in set($row, $col) in $this" }
        check(row in 0..<height) { "Can't access row $row in set($row, $col) in $this" }
        data[idx(col, row)] = value
    }

    /**
     * Updates the value at the given coordinate.
     * @param col the index of the column
     * @param row the index of the row
     * @param operation the update operation to apply
     * @return the new value
     */
    fun update(
        col: Int,
        row: Int,
        operation: (Double) -> Double,
    ): Double {
        check(col in 0..<width) { "Can't access column $col in set($row, $col) in $this" }
        check(row in 0..<height) { "Can't access row $row in set($row, $col) in $this" }
        val updated = operation(data[idx(col, row)])
        data[idx(col, row)] = updated
        return updated
    }

    /**
     * Extracts the nth column as a vertical Matrix.
     * @param col the column to extract
     * @return the column as a Matrix
     */
    fun column(col: Int): Matrix<Dimension.D1, R> {
        return Matrix(
            DoubleArray(height) { row -> get(col, row) },
            1,
            height,
        )
    }

    /**
     * Extracts the nth row as a horizontal Matrix.
     * @param row the row to extract
     * @return the row as a Matrix
     */
    fun row(row: Int): Matrix<C, Dimension.D1> {
        return Matrix(
            DoubleArray(width) { col -> get(col, row) },
            width,
            1,
        )
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

    // region Debug

    /**
     * Prints the matrix to the standard output.
     */
    fun debugPrint() {
        val asStr = data.map { it.toString() }
        val maxWidth = asStr.maxBy { it.length }.length

        for (j in 0..<height) {
            for (i in 0..<width) {
                print(asStr[idx(i, j)].padStart(maxWidth, ' '))
                print("   ")
            }
            println()
        }
    }

    // endregion

    // region Internal

    internal fun coords(index: Int): Pair<Int, Int> {
        val col = index.mod(width)
        val row = (index - col) / width
        return col to row
    }

    internal fun idx(col: Int, row: Int): Int {
        return (row * width) + col
    }

    internal fun swapRows(rowA: Int, rowB: Int) {
        for (col in 0 until width) {
            val tmp = data[idx(col, rowA)]
            data[idx(col, rowA)] = data[idx(col, rowB)]
            data[idx(col, rowB)] = tmp
        }
    }

    private fun rrefEnsurePivot(marchingRow: Int, marchingCol: Int): Double {
        var pivot = data[idx(marchingCol, marchingRow)]
        if (abs(pivot) <= EPSILON) {
            var swapWithRow = -1
            for (row in (marchingRow + 1) until height) {
                pivot = data[idx(marchingCol, row)]
                if (abs(pivot) > EPSILON) {
                    swapWithRow = row
                    break
                }
            }

            if (swapWithRow < 0) {
                return 0.0
            }

            swapRows(marchingRow, swapWithRow)
        }
        return pivot
    }

    private fun rrefScaleRow(marchingRow: Int, marchingCol: Int) {
        val scale = 1.0 / data[idx(marchingCol, marchingRow)]
        for (col in marchingCol until width) {
            update(col, marchingRow) { it * scale }
        }
    }

    private fun rrefReduceOtherRows(marchingRow: Int, marchingCol: Int) {
        for (row in 0 until height) {
            if (row != marchingRow) {
                val factor = data[idx(marchingCol, row)]
                for (col in (marchingCol) until width) {
                    update(col, row) { it - (factor * get(col, marchingRow)) }
                }
            }
        }
    }

    // endregion

    companion object {

        /**
         * Creates a Matrix with no specific dimension type. This is very unsafe and should only be used
         * if you know what you're doing.
         * @param cols the column dimension
         * @param rows the row dimension
         * @return a matrix filled with 0
         */
        fun unsafeMatrix(cols: Int, rows: Int): Matrix<Dimension, Dimension> {
            return Matrix(
                data = DoubleArray(cols * rows) { 0.0 },
                width = cols,
                height = rows,
            )
        }

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
            return Matrix<C, R>(
                data = DoubleArray(cols.size * rows.size),
                width = cols.size,
                height = rows.size,
            ).apply {
                for (i in 0 until min(cols.size, rows.size)) {
                    set(i, i, 1.0)
                }
            }
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
