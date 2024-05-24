package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.math.Dimension
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element

fun dimensionArb() = Arb.element(
    Dimension.D1,
    Dimension.D2,
    Dimension.D3,
    Dimension.D4,
)
