package art.luxels.components.test.kotest.property

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.list

fun <D : Dimension> vectorArb(d: D) = arbitrary {
    Vector<D>(
        DoubleArray(d.size) {
            doubleArb().bind()
        },
    )
}

fun <D : Dimension> vectorListArb(d: D) = arbitrary {
    Arb.list(vectorArb(d)).bind()
}
