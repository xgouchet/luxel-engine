package art.luxels.core.test.kotest.property

import art.luxels.imageio.color.HDRColor
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double

fun colorArb() = arbitrary {
    HDRColor(
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
        Arb.double(-SMALL_DOUBLE, LARGE_DOUBLE).bind(),
    )
}
