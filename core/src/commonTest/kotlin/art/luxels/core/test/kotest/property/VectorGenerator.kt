package art.luxels.core.test.kotest.property

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import io.kotest.property.arbitrary.arbitrary

fun <D : Dimension> vectorArb(d: D) = arbitrary {
    Vector<D>(
        DoubleArray(d.size) {
            doubleSmallArb().bind()
        },
    )
}
