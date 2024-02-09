package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.position.Box
import io.kotest.property.arbitrary.arbitrary

fun boxArb() = arbitrary {
    val min = vector3Arb().bind()
    val size = vector3Arb().bind().abs()
    Box(
        min,
        min + size,
    )
}
