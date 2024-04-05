package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.math.geometry.Vector3
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double

fun vector3Arb() = arbitrary {
    Vector3(
        Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false).bind(),
        Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false).bind(),
        Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false).bind(),
    )
}

fun vector3SmallArb() = arbitrary {
    Vector3(
        Arb.double(-SMALL_DOUBLE, SMALL_DOUBLE, false).bind(),
        Arb.double(-SMALL_DOUBLE, SMALL_DOUBLE, false).bind(),
        Arb.double(-SMALL_DOUBLE, SMALL_DOUBLE, false).bind(),
    )
}
