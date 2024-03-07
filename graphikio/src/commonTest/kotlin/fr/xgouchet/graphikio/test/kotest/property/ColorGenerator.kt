package fr.xgouchet.graphikio.test.kotest.property

import fr.xgouchet.graphikio.color.BoundColor
import fr.xgouchet.graphikio.color.UnboundColor
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int

fun unboundColorArb() = arbitrary {
    UnboundColor(
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
    )
}

fun boundColorArb() = arbitrary {
    BoundColor(
        Arb.int(0, 255).bind(),
        Arb.int(0, 255).bind(),
        Arb.int(0, 255).bind(),
        Arb.int(0, 255).bind(),
    )
}
