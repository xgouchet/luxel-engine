package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.position.Rectangle
import io.kotest.property.arbitrary.arbitrary

fun rectangleArb() = arbitrary {
    val min = vector2Arb().bind()
    val size = vector2Arb().bind().abs()
    Rectangle(
        min,
        min + size,
    )
}
