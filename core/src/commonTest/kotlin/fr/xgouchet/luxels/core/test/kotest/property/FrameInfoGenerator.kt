package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.render.FrameInfo
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import kotlin.time.Duration.Companion.nanoseconds

internal fun frameInfoArb() = arbitrary {
    FrameInfo(
        Arb.int(0, 65536).bind(),
        Arb.long(0, Int.MAX_VALUE.toLong()).bind().nanoseconds,
    )
}
