package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import io.kotest.property.arbitrary.arbitrary

fun <D : Dimension> vectorArb(d: D) = arbitrary {
    Vector<D>(
        DoubleArray(d.size) {
            doubleArb().bind()
        },
    )
}
