package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.color.Color
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double

fun colorArb() = arbitrary {
    Color(
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
    )
}
