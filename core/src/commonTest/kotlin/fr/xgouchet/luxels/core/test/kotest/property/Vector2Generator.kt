package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.math.Vector2
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double

fun vector2Arb() = arbitrary {
    Vector2(
        Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false).bind(),
        Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false).bind(),
    )
}
