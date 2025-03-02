package art.luxels.components.test.kotest.property

import art.luxels.imageio.color.HDRColor
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double

fun colorArb() = arbitrary {
    HDRColor(
        Arb.double(0.0, 1.0).bind(),
        Arb.double(0.0, 1.0).bind(),
        Arb.double(0.0, 1.0).bind(),
        Arb.double(0.0, 1.0).bind(),
    )
}
