package art.luxels.core.test.kotest.property

import art.luxels.core.math.Dimension
import art.luxels.core.math.Matrix
import io.kotest.property.arbitrary.arbitrary

fun <C : Dimension, R : Dimension> matrixArb(cols: C, rows: R) = arbitrary {
    Matrix<C, R>(
        data = DoubleArray(cols.size * rows.size) {
            doubleSmallArb().bind()
        },
        width = cols.size,
        height = rows.size,
    )
}
