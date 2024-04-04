package fr.xgouchet.graphikio.test.kotest.property

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.color.SDRColor
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int

fun unboundColorArb() = arbitrary {
    HDRColor(
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
    )
}

fun boundColorArb() = arbitrary {
    SDRColor(
        Arb.int(0, 255).bind(),
        Arb.int(0, 255).bind(),
        Arb.int(0, 255).bind(),
        Arb.int(0, 255).bind(),
    )
}
