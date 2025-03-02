package art.luxels.engine.test.kotest.property

import art.luxels.core.math.Dimension
import io.kotest.property.Exhaustive
import io.kotest.property.exhaustive.of

fun dimensionArb() = Exhaustive.of(
    Dimension.D1,
    Dimension.D2,
    Dimension.D3,
    Dimension.D4,
).toArb()
