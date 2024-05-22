package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Matrix
import io.kotest.property.arbitrary.arbitrary

fun <C : Dimension, R : Dimension> matrixArb(cols: C, rows: R) = arbitrary {
    Matrix<C, R>(
        data = DoubleArray(cols.size * rows.size) {
            doubleArb().bind()
        },
        width = cols.size,
        height = rows.size,
    )
}
