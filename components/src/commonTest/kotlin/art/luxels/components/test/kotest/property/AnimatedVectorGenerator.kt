package art.luxels.components.test.kotest.property

import art.luxels.components.animation.AnimatedVector
import art.luxels.core.math.Dimension
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.list

fun <D : Dimension> animatedVectorArb(d: D) = arbitrary {
    AnimatedVector(
        shortDurationArb().bind(),
        Arb.list(vectorArb(d), range = 1..16).bind(),
    )
}

fun <D : Dimension> animatedVectorListArb(d: D) = arbitrary {
    Arb.list(animatedVectorArb(d)).bind()
}
