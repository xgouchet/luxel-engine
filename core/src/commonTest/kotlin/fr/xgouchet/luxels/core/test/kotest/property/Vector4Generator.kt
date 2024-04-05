package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.math.geometry.Vector4
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double

fun vector4Arb() = arbitrary {
    Vector4(
        Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false).bind(),
        Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false).bind(),
        Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false).bind(),
        Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false).bind(),
    )
}
