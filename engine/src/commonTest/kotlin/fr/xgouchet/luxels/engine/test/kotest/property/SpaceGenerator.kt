package fr.xgouchet.luxels.engine.test.kotest.property

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import io.kotest.property.arbitrary.arbitrary

fun <D : Dimension> volumeArb(d: D) = arbitrary {
    val min = vectorArb(d).bind()
    val size = vectorArb(d).bind().abs()
    Volume(min = min, max = min + size)
}
