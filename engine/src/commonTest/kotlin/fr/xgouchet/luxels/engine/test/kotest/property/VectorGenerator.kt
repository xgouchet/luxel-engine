package fr.xgouchet.luxels.engine.test.kotest.property

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import io.kotest.property.arbitrary.arbitrary

fun <D : Dimension> vectorArb(d: D) = arbitrary {
    val data = DoubleArray(d.size) {
        doubleArb().bind()
    }
    Vector.from(d) { data[it] }
}

fun vectorArb() = arbitrary {
    val dimension = dimensionArb().bind()
    val data = DoubleArray(dimension.size) {
        doubleArb().bind()
    }
    Vector.from(dimension) { data[it] }
}
